# 🪵 El Grillo Tallados - Tienda de Artesanías en Madera

![Banner El Grillo Tallados](https://via.placeholder.com/1200x300/6db33f/ffffff?text=El+Grillo+Tallados)

## 🌟 Visión General

**El Grillo Tallados** tienda online de tallados en madera, desarrollado con Spring Boot y diseñado siguiendo los principios de Domain-Driven Design (DDD). Esta plataforma ofrece una experiencia de compra intuitiva y atractiva, conectando artesanos tradicionales con clientes de todo el país.


## ✨ Características Principales

### 📦 Para Clientes
- **Catálogo Intuitivo**: Navegación sencilla por categorías de productos 
- **Búsqueda Avanzada**: Encuentra fácilmente el tallado perfecto
- **Carrito de Compras**: Experiencia de compra fluida y segura
- **Seguimiento de Pedidos**: Historial completo y estado de cada pedido
- **Perfiles Personalizados**: Gestión de información personal y preferencias

### 🛠️ Para Desarrolladores
- **Arquitectura DDD**: Separación clara de dominios (Producto, Cliente, Pedido)
- **Principios SOLID**: Código mantenible y extensible
- **Fragmentos Reutilizables**: Componentes Thymeleaf para máxima reutilización
- **Diseño Responsive**: Experiencia óptima en cualquier dispositivo
- **Seguridad Integrada**: Autenticación y autorización con Spring Security

## 🏗️ Arquitectura
El proyecto sigue una arquitectura limpia basada en dominios:

com.elgrillo.demo/
├── src/
│   ├── main/
│   │   ├── java/com/elgrillo/demo/
│   │   │   ├── cliente/               # Gestión de usuarios y autenticación
│   │   │   │   ├── controller/        # Controladores REST y de vistas
│   │   │   │   ├── model/             # Entidades y objetos de valor
│   │   │   │   ├── repository/        # Acceso a datos
│   │   │   │   └── service/           # Lógica de negocio
│   │   │   │
│   │   │   ├── producto/              # Gestión del catálogo de tallados
│   │   │   │   ├── controller/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   └── service/
│   │   │   │
│   │   │   ├── pedido/                # Carrito y procesamiento de órdenes
│   │   │   │   ├── controller/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   └── service/
│   │   │   │
│   │   │   ├── config/                # Configuración de la aplicación
│   │   │   ├── controller/            # Controladores globales
│   │   │   └── util/                  # Utilidades comunes
│   │   │
│   │   └── resources/
│   │       ├── static/                # Recursos estáticos
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── images/
│   │       │
│   │       └── templates/             # Plantillas Thymeleaf
│   │           ├── fragments/         # Componentes reutilizables
│   │           ├── layouts/           # Plantillas base
│   │           ├── clientes/          # Vistas de clientes
│   │           ├── productos/         # Vistas de productos
│   │           └── pedidos/           # Vistas de pedidos

```

Cada dominio está estructurado en capas:
- **Model**: Entidades y objetos de valor
- **Repository**: Acceso a datos
- **Service**: Lógica de negocio
- **Controller**: Gestión de peticiones HTTP

## 🎨 Interfaz de Usuario

La interfaz utiliza Thymeleaf con Tailwind CSS para crear una experiencia visual atractiva y coherente:

- **Plantilla Base**: Estructura común para todas las páginas
- **Fragmentos Reutilizables**: Componentes modulares para máxima consistencia
- **Diseño Adaptativo**: Funciona perfectamente en dispositivos móviles y de escritorio

## 🚀 Tecnologías Utilizadas

- **Backend**: Spring Boot 3.0, Java 17
- **Persistencia**: Spring Data JPA, Hibernate
- **Frontend**: Thymeleaf, Tailwind CSS
- **Seguridad**: Spring Security
- **Base de Datos**: PostgreSQL
- **Herramientas**: Maven, Spring DevTools

## 🔧 Instalación y Ejecución

```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/elgrillotallados.git

# Navegar al directorio
cd elgrillotallados

# Compilar y ejecutar
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## 📸 Capturas de Pantalla

![Página de inicio](https://via.placeholder.com/600x300/6db33f/ffffff?text=Página+de+Inicio)
![Catálogo de productos](https://via.placeholder.com/600x300/1b1f23/ffffff?text=Catálogo+de+Productos)
![Detalle de producto](https://via.placeholder.com/600x300/d4a01f/ffffff?text=Detalle+de+Producto)

## 🤝 Contribuciones

¡Las contribuciones son bienvenidas! Si deseas mejorar El Grillo Tallados:

1. Haz fork del repositorio
2. Crea una rama para tu característica (`git checkout -b feature/amazing-feature`)
3. Realiza tus cambios y haz commit (`git commit -m 'Add some amazing feature'`)
4. Sube tus cambios (`git push origin feature/amazing-feature`)
5. Abre un Pull Request



by Federico