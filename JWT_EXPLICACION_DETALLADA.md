# 🔐 Explicación Detallada: Cómo Funciona JWT en Tu Proyecto

## ✅ **SÍ, el token es ÚNICO y contiene TU información**

### **Concepto Clave:**

```
Token JWT = Tarjeta de Identificación Digital

┌─────────────────────────────────────┐
│  Token de JUAN                       │
├─────────────────────────────────────┤
│  Nombre: juan                        │
│  Creado: 14:00:00                    │
│  Expira: 15:00:00                    │
│  Firma: abc123xyz... (único)         │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│  Token de MARÍA                      │
├─────────────────────────────────────┤
│  Nombre: maria                       │
│  Creado: 14:05:00                    │
│  Expira: 15:05:00                    │
│  Firma: xyz789abc... (único)         │
└─────────────────────────────────────┘
```

Cada usuario tiene su propio token con su información.

---

## 🔍 **Ejemplo REAL paso a paso:**

### **1. Juan hace LOGIN**

```java
// AuthService.java - login()
public AuthResponseDto login(LoginDto loginDto) {
    // Usuario ingresa: username="juan", password="password123"
    
    // Spring Security valida las credenciales
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken("juan", "password123")
    );
    
    // Si llegó aquí → credenciales correctas ✅
    UserEntity user = (UserEntity) authentication.getPrincipal();
    // user.getUsername() = "juan"
    // user.getId() = 1
    // user.getRole() = USER
    
    // 🎫 GENERAR TOKEN ÚNICO PARA JUAN
    String token = jwtService.generateToken(user);
    
    return new AuthResponseDto(token, "Bearer", 3600, userInfo);
}
```

### **2. ¿Qué hace `generateToken(user)`?**

```java
// JwtService.java
public String generateToken(UserDetails userDetails) {
    // userDetails.getUsername() = "juan"
    
    return Jwts.builder()
        .setSubject("juan")  // ← GUARDA el username dentro del token
        .setIssuedAt(new Date(1728584400000))  // Ahora: 14:00:00
        .setExpiration(new Date(1728588000000))  // Expira: 15:00:00 (1 hora después)
        .signWith(getSignInKey(), HS256)  // Firma con SECRET_KEY
        .compact();
}
```

**Resultado - Token de Juan:**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuIiwiaWF0IjoxNzI4NTg0NDAwLCJleHAiOjE3Mjg1ODgwMDB9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c

└────────────────────┬───────────────────┘ └──────────────────────┬─────────────────────────┘ └─────────────────┬────────────────┘
      HEADER (info del algoritmo)          PAYLOAD (datos de juan)                    SIGNATURE (firma única)
```

### **3. Juan crea una REVIEW**

```bash
POST /reviews
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Body: {
  "movieId": 1,
  "rating": 5,
  "comment": "Excelente película!"
}
```

### **4. JwtAuthenticationFilter EXTRAE la información del token**

```java
// JwtAuthenticationFilter.java
protected void doFilterInternal(HttpServletRequest request, ...) {
    // 1. Extraer token del header
    String jwt = request.getHeader("Authorization").substring(7);
    // jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    
    // 2. 🔍 LEER username DEL TOKEN (sin ir a BD)
    String username = jwtService.extractUsername(jwt);
    // username = "juan" ← ¡LEÍDO DEL TOKEN!
    
    // 3. Validar firma (sin BD)
    if (jwtService.isTokenValid(jwt, userDetails)) {
        // 4. Autenticar en Spring Security
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
```

### **5. ReviewController RECIBE al usuario autenticado**

```java
// ReviewController.java
@PostMapping
public ResponseEntity<ReviewDto> createReview(
    @Valid @RequestBody CreateReviewDto createReviewDto,
    @AuthenticationPrincipal UserEntity user  // ← Spring inyecta automáticamente
) {
    // user.getUsername() = "juan"
    // user.getId() = 1
    
    // Crear review CON EL ID DEL USUARIO DEL TOKEN
    ReviewDto review = reviewService.createReview(createReviewDto, user.getId());
    // La review se guarda con user_id=1 (Juan)
    
    return ResponseEntity.status(CREATED).body(review);
}
```

---

## 🎯 **Lo más importante:**

### **El token es como tu DNI digital:**

```
┌──────────────────────────────────────────────────┐
│  DNI Real                                        │
├──────────────────────────────────────────────────┤
│  Nombre: Juan Pérez                              │
│  DNI: 12345678                                   │
│  Fecha nacimiento: 01/01/1990                    │
│  Firma: [hologram imposible de falsificar]      │
│                                                  │
│  Cuando muestras tu DNI:                         │
│  - No llaman a RENIEC a verificar                │
│  - Solo verifican que el DNI sea legítimo        │
│  - Leen tu nombre del DNI mismo                  │
└──────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────┐
│  JWT Token                                       │
├──────────────────────────────────────────────────┤
│  Username: juan                                  │
│  User ID: 1                                      │
│  Creado: 14:00:00                                │
│  Expira: 15:00:00                                │
│  Firma: [hash imposible de falsificar]          │
│                                                  │
│  Cuando envías tu token:                         │
│  - No buscan en BD para validar                  │
│  - Solo verifican que el token sea legítimo      │
│  - Leen tu username del token mismo              │
└──────────────────────────────────────────────────┘
```

---

## 🔄 **Flujo Completo con el Token:**

```
1️⃣  LOGIN
    Juan → username="juan", password="pass123"
    Servidor → Valida credenciales en BD ✅
    Servidor → Genera token único con "juan" dentro
    Juan recibe → eyJhbGci... (token con "juan" dentro)

2️⃣  GUARDAR TOKEN (Frontend)
    localStorage.setItem("token", "eyJhbGci...")

3️⃣  CREAR REVIEW (Juan está autenticado)
    Request:
      POST /reviews
      Authorization: Bearer eyJhbGci...
      Body: { movieId: 1, rating: 5 }
    
    Servidor:
      - Lee token → extrae username="juan" del token
      - Valida firma → ✅ firma correcta
      - Valida expiración → ✅ no expirado
      - Busca usuario en BD → user_id=1
      - Ejecuta: createReview(dto, user_id=1)
      - Guarda review con user_id=1
    
    Response:
      {
        "id": 1,
        "userId": 1,  // ← ID de Juan
        "username": "juan",  // ← Username de Juan
        "movieId": 1,
        "rating": 5,
        "comment": "Excelente!"
      }

4️⃣  VER MIS REVIEWS
    Request:
      GET /reviews/user/1
      Authorization: Bearer eyJhbGci...
    
    Servidor:
      - Lee token → username="juan"
      - Valida token → ✅
      - Retorna todas las reviews del user_id=1

5️⃣  EDITAR MI REVIEW (solo puedo editar las mías)
    Request:
      PUT /reviews/1
      Authorization: Bearer eyJhbGci...
      Body: { rating: 4, comment: "Buena" }
    
    Servidor:
      - Lee token → username="juan", user_id=1
      - Busca review_id=1 en BD
      - Verifica: review.user_id == 1 ✅
      - Permite edición

6️⃣  INTENTAR EDITAR REVIEW DE OTRO USUARIO
    Request:
      PUT /reviews/2  // Review de María (user_id=2)
      Authorization: Bearer eyJhbGci... (token de Juan)
      Body: { rating: 1 }
    
    Servidor:
      - Lee token → username="juan", user_id=1
      - Busca review_id=2 en BD
      - Verifica: review.user_id == 2 ❌ (no es de Juan)
      - Response: 403 Forbidden "No puedes editar esta review"
```

---

## 🎨 **Visualización del Token:**

Si usas https://jwt.io/ y pegas tu token, verás algo así:

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
.
{
  "sub": "juan",           // ← ESTO ES LO IMPORTANTE
  "iat": 1728584400,       // ← Emitido: 2025-10-10 14:00:00
  "exp": 1728588000        // ← Expira: 2025-10-10 15:00:00
}
.
SIGNATURE_HASH
```

**Cada vez que haces un request:**
- El servidor lee "juan" del token
- Sabe que ERES juan
- No necesita preguntarle a la BD "¿quién es el dueño de este token?"
- Solo verifica que el token no esté falsificado

---

## 💡 **Preguntas Frecuentes:**

### **Q: ¿El token cambia cada vez que hago login?**
**A:** Sí, cada login genera un nuevo token con nueva fecha de expiración.

### **Q: ¿Puedo usar el mismo token desde el celular y la web?**
**A:** Sí, porque el token solo contiene tu username, no el dispositivo.

### **Q: ¿Qué pasa si alguien roba mi token?**
**A:** Puede usarlo hasta que expire (1 hora). Por eso es importante:
- Usar HTTPS (encriptación)
- Guardar tokens de forma segura
- Tiempos de expiración cortos

### **Q: ¿Por qué el token expira?**
**A:** Seguridad. Si alguien lo roba, solo funciona 1 hora. Después debes hacer login de nuevo.

### **Q: ¿Puedo ver qué hay dentro de mi token?**
**A:** Sí, el token NO está encriptado, solo FIRMADO. Cualquiera puede leerlo, pero nadie puede modificarlo sin romper la firma.

### **Q: ¿Entonces alguien puede leer mi token y ver mi username?**
**A:** Sí, pero NO pueden:
- Modificar el username
- Crear un token falso
- Extender la fecha de expiración
Porque no tienen la SECRET_KEY para generar la firma correcta.

---

## ✨ **Resumen Final:**

```
✅ Token = Tu identificación única
✅ Contiene tu username dentro
✅ Cada usuario tiene su propio token
✅ El servidor lee tu username DEL TOKEN
✅ No necesita ir a BD para saber quién eres
✅ Solo valida que el token sea legítimo (firma correcta)
✅ Si intentas modificar el token → firma inválida → rechazado
```

¡Eso es JWT en pocas palabras! 🚀

