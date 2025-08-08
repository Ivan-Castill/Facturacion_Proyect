-- Facturacion para Un supermercado
-- Ivan Andres Castillo Caiza
-- De forma local. 
-- Usuarios con sus privilegios y restricciones

-- usuario de validacion
CREATE USER 'validadorlocal'@'localhost' IDENTIFIED BY 'validador2025';
GRANT SELECT ON supermercadodb.usuarios TO 'validadorlocal'@'localhost';
FLUSH PRIVILEGES;

-- Usuario administrador: todos los permisos sobre toda la base
CREATE USER 'adminlocal'@'localhost' IDENTIFIED BY 'admin2025';
GRANT ALL PRIVILEGES ON supermercadodb.* TO 'adminlocal'@'localhost';
FLUSH PRIVILEGES;

-- Usuario cajero: solo puede insertar clientes, facturas y detalle_factura
CREATE USER 'cajerolocal'@'localhost' IDENTIFIED BY 'cajero2025';
GRANT INSERT, SELECT ON supermercadodb.cliente TO 'cajerolocal'@'localhost';
GRANT SELECT ON supermercadodb.productos TO 'cajerolocal'@'localhost';
GRANT INSERT ON supermercadodb.facturas TO 'cajerolocal'@'localhost';
GRANT INSERT ON supermercadodb.detalle_factura TO 'cajerolocal'@'localhost';
FLUSH PRIVILEGES;

-- Usuario servidor_bodega: puede insertar, actualizar y eliminar en productos
CREATE USER 'servidor_bodegalocal'@'localhost' IDENTIFIED BY 'bodega2025';
GRANT INSERT, UPDATE, DELETE, SELECT ON supermercadodb.productos TO 'servidor_bodegalocal'@'localhost';
FLUSH PRIVILEGES;

DROP USER 'validadorlocal'@'localhost';
DROP USER 'adminlocal'@'localhost';
DROP USER 'cajerolocal'@'localhost';
DROP USER 'servidor_bodegalocal'@'localhost';

