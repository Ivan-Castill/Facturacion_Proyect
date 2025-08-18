# Sistema de Facturación para Supermercado

Este proyecto es un sistema de facturación desarrollado en **Java** con interfaz gráfica **Swing** y base de datos **MySQL**. Permite registrar ventas, administrar clientes, gestionar inventario y generar facturas en formato JSON. Además, cuenta con diferentes módulos según el rol de usuario: cajero, administrador y servidor de bodega.

---

## Características

- **Login seguro** según el rol del usuario.
- **Módulo Caja Registradora:** registrar clientes, agregar productos a facturas, calcular totales, IVA y generar facturas en JSON.
- **Módulo Servidor Bodega:** registrar, actualizar y eliminar productos del inventario.
- **Módulo Administrador:** gestionar usuarios, roles y revisar historiales.
- Exportación de facturas a **JSON** para llevar un registro detallado.
- Conexión en **LAN** para trabajar de manera remota entre varias computadoras en la misma red local.

---

## Estructura de la Base de Datos

**Base de datos:** `facturacion_db`

### Tabla: clientes
- `id_cliente` (INT, PK, AUTO_INCREMENT)
- `cedula` (VARCHAR(15), UNIQUE)
- `nombres` (VARCHAR(100))
- `apellidos` (VARCHAR(100))
- `direccion` (VARCHAR(200))
- `telefono` (VARCHAR(20))
- `email` (VARCHAR(100))  
Permite registrar y consultar la información básica de los clientes.

### Tabla: productos
- `id_producto` (INT, PK, AUTO_INCREMENT)
- `codigo` (VARCHAR(20), UNIQUE)
- `nombre` (VARCHAR(100))
- `descripcion` (TEXT)
- `precio` (DECIMAL(10,2))
- `stock` (INT)  
Contiene los datos de cada producto, incluyendo su precio y el inventario disponible.

### Tabla: facturas
- `id_factura` (INT, PK, AUTO_INCREMENT)
- `numero_factura` (VARCHAR(20), UNIQUE)
- `fecha` (DATETIME)
- `id_cliente` (INT, FK → clientes.id_cliente)
- `total` (DECIMAL(10,2))  
Representa la cabecera de cada factura.

### Tabla: detalle_factura
- `id_detalle` (INT, PK, AUTO_INCREMENT)
- `id_factura` (INT, FK → facturas.id_factura)
- `id_producto` (INT, FK → productos.id_producto)
- `cantidad` (INT)
- `precio_unitario` (DECIMAL(10,2))
- `subtotal` (DECIMAL(10,2))  
Registra los productos incluidos en cada factura.

### Tabla: usuarios
- `id_usuario` (INT, PK, AUTO_INCREMENT)
- `usuario` (VARCHAR(50), UNIQUE)
- `clave` (VARCHAR(255))
- `rol` (ENUM('admin','empleado'))  
Maneja los accesos y define roles de usuario.

---

## Dependencias

Para que el proyecto funcione correctamente, se requieren las siguientes librerías:

1. **MySQL Connector/J** (Conexión con MySQL)  
[Enlace oficial](https://dev.mysql.com/downloads/connector/j/)  
