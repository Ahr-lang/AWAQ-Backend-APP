# Componentes Reutilizables AWAQ

Esta carpeta contiene componentes UI reutilizables que implementan el sistema de diseño de AWAQ.

## 📦 Componentes Disponibles

### 1. **AwaqTextField.kt**
Campos de texto personalizados con el estilo AWAQ.

#### Componentes:
- `AwaqTextField` - TextField genérico personalizado
- `EmailTextField` - Campo especializado para email con icono
- `PasswordTextField` - Campo de contraseña con toggle de visibilidad

```kotlin
// Uso básico
EmailTextField(
    value = email,
    onValueChange = { email = it },
    modifier = Modifier.fillMaxWidth()
)

PasswordTextField(
    value = password,
    onValueChange = { password = it },
    modifier = Modifier.fillMaxWidth()
)
```

---

### 2. **AwaqButton.kt**
Botones con el estilo AWAQ.

#### Componentes:
- `AwaqPrimaryButton` - Botón primario con soporte para loading
- `AwaqSecondaryButton` - Botón secundario (outline)

```kotlin
// Botón primario
AwaqPrimaryButton(
    text = "Iniciar sesión",
    onClick = { /* acción */ },
    enabled = true,
    isLoading = false
)

// Botón secundario
AwaqSecondaryButton(
    text = "Ver Tareas",
    onClick = { /* acción */ }
)
```

---

### 3. **AwaqCard.kt**
Cards estilizadas para contenido.

#### Componentes:
- `AwaqCard` - Card principal con elevación alta
- `AwaqSimpleCard` - Card simple para contenido general

```kotlin
AwaqCard {
    // Contenido con padding automático
    Text("Título")
    Spacer(Modifier.height(8.dp))
    Text("Descripción")
}
```

---

### 4. **AwaqBranding.kt**
Componentes de branding y headers.

#### Componentes:
- `AwaqLogo` - Logo de AWAQ
- `AwaqHeader` - Header con logo, título y subtítulo

```kotlin
AwaqHeader(
    title = "Bienvenido a AWAQ",
    subtitle = "Inicia sesión para continuar"
)
```

---

### 5. **AwaqStateHandlers.kt**
Componentes para manejar estados de la UI.

#### Componentes:
- `LoadingIndicator` - Indicador de carga centrado
- `ErrorMessage` - Mensaje de error estilizado
- `LoadingOverlay` - Overlay de carga animado
- `EmptyState` - Mensaje cuando no hay datos

```kotlin
// Indicador de carga
LoadingIndicator(message = "Cargando datos...")

// Mensaje de error
ErrorMessage(
    message = "Error al cargar datos",
    onRetry = { /* reintentar */ }
)

// Overlay
LoadingOverlay(isVisible = isLoading)

// Estado vacío
EmptyState(
    message = "No hay usuarios disponibles",
    icon = "👥"
)
```

---

### 6. **AwaqBackground.kt**
Fondo con gradiente estándar de AWAQ.

```kotlin
AwaqBackground {
    // Contenido con gradiente verde suave de fondo
    Column { /* ... */ }
}
```

---

### 7. **AwaqDialogs.kt**
Diálogos estandarizados.

#### Componentes:
- `AwaqConfirmDialog` - Diálogo de confirmación
- `AwaqInfoDialog` - Diálogo informativo

```kotlin
AwaqConfirmDialog(
    title = "Eliminar usuario",
    message = "¿Estás seguro de eliminar este usuario?",
    confirmText = "Eliminar",
    dismissText = "Cancelar",
    onConfirm = { /* acción */ },
    onDismiss = { /* cerrar */ }
)
```

---

### 8. **TenantSelector.kt**
Selector de tenants reutilizable.

```kotlin
TenantSelector(
    tenants = listOf("awaq", "company1", "company2"),
    currentTenant = currentTenant,
    onTenantSelected = { tenant -> viewModel.setTenant(tenant) }
)
```

---

## 🎨 Paleta de Colores

Los componentes utilizan la paleta estándar de AWAQ:

```kotlin
val Ink = Color(0xFF111111)           // Texto principal
val SubtleText = Color(0xFF50565A)    // Texto secundario
val FieldBorder = Color(0xFFCBD5D1)   // Bordes de campos
val GreenSoft = AwaqGreen.copy(alpha = 0.08f) // Fondo suave
val DividerSoft = Color(0xFFE6E8EA)   // Divisores
```

---

## ✅ Beneficios

1. **Consistencia** - Todos los componentes siguen el mismo sistema de diseño
2. **Mantenibilidad** - Cambios centralizados en un solo lugar
3. **Reutilización** - Menos código duplicado
4. **Productividad** - Desarrollo más rápido de nuevas pantallas
5. **Accesibilidad** - Estados y feedback consistentes

---

## 📝 Ejemplo de Refactorización

**Antes:**
```kotlin
OutlinedTextField(
    value = email,
    onValueChange = { email = it },
    label = { Text("Correo electrónico") },
    leadingIcon = { Icon(Icons.Outlined.Email, "email", tint = AwaqGreen) },
    singleLine = true,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = AwaqGreen,
        unfocusedBorderColor = FieldBorder,
        // ... muchas más líneas
    )
)
```

**Después:**
```kotlin
EmailTextField(
    value = email,
    onValueChange = { email = it },
    modifier = Modifier.fillMaxWidth()
)
```

---

## 🚀 Próximos Pasos

Considera crear componentes adicionales para:
- Tarjetas de usuario en listas
- Componentes de navegación personalizados
- Gráficos y visualizaciones de datos
- Chips y badges
- Bottom sheets personalizados
