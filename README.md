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


## Tecnologías
- **Java 8+** (Swing para la interfaz gráfica)
- **MySQL** (gestión de la base de datos)
- **Gson** (para exportar facturas a JSON)
- **JDBC** (para la conexión entre Java y MySQL)

## Funcionalidades principales

### 1. Login
- Validación de usuarios mediante credenciales.
- Determinación automática del rol (cajero, administrador, servidor de bodega).
- Seguridad en el acceso a la información.

### 2. Caja Registradora
- Registro y validación de clientes.
- Selección de productos mediante ID y cantidad.
- Cálculo automático de subtotal, IVA y total general.
- Guardado de facturas en la base de datos y en formato JSON.
- Interfaz intuitiva y rápida para ventas diarias.

### 3. Servidor Bodega
- Registro, actualización y eliminación de productos.
- Visualización del inventario en tiempo real mediante tabla.
- Botón para refrescar datos y mantener la información siempre actualizada.

### 4. Administrador
- Gestión completa de usuarios y roles.
- Revisión de historiales y generación de reportes.
- Control integral del sistema y supervisión de todas las operaciones.
- Cierre de sesión seguro para mantener la integridad de los datos.

## Dependencias

Para que el proyecto funcione correctamente, se requieren las siguientes librerías:

1. **MySQL Connector/J** (Conexión con MySQL)  
[Enlace oficial](https://dev.mysql.com/downloads/connector/j/)  
