-- Facturacion para Un supermercado
-- Ivan Andres Castillo Caiza
-- De forma local. 
-- Usuarios con sus privilegios y restricciones

-- usuario de validacion
CREATE USER 'validadorlan'@'%' IDENTIFIED BY 'validador2025';
GRANT SELECT ON supermercadodb.usuarios TO 'validadorlan'@'%';
FLUSH PRIVILEGES;

-- Usuario administrador
CREATE USER 'adminlan'@'%' IDENTIFIED BY 'admin2025';
GRANT ALL PRIVILEGES ON supermercadodb.* TO 'adminlan'@'%';
FLUSH PRIVILEGES;

-- Usuario cajero
CREATE USER 'cajerolan'@'%' IDENTIFIED BY 'cajero2025';
GRANT INSERT, SELECT ON supermercadodb.cliente TO 'cajerolan'@'%';
GRANT SELECT ON supermercadodb.productos TO 'cajerolan'@'%';
GRANT INSERT ON supermercadodb.facturas TO 'cajerolan'@'%';
GRANT INSERT ON supermercadodb.detalle_factura TO 'cajerolan'@'%';
FLUSH PRIVILEGES;

-- Usuario servidor_bodega
CREATE USER 'servidor_bodegalan'@'%' IDENTIFIED BY 'bodega2025';
GRANT INSERT, UPDATE, DELETE, SELECT ON supermercadodb.productos TO 'servidor_bodegalan'@'%';
FLUSH PRIVILEGES;



DROP USER 'validadorlan'@'%';
DROP USER 'adminlan'@'%';
DROP USER 'cajerolan'@'%';
DROP USER 'servidor_bodegalan'@'%';

