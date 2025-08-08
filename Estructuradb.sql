-- Facturacion para Un supermercado
-- Ivan Andres Castillo Caiza
-- 1. Crear base de datos
create database supermercadodb;
use supermercadodb;

-- 2. Tabla de usuarios del sistema
create table usuarios(
	id_user int auto_increment primary key,
    fecha_registro datetime default current_timestamp,
    usuario varchar(100) unique not null,
    pass varchar(255) not null,
    rol enum('Administrador','Cajero','Servidor bodega')
);
-- 3. Tabla de personal
create table personal(
	id_personal int auto_increment primary key,
    fecha_registro datetime default current_timestamp,
    nombres varchar(100) not null,
    telefono varchar(20),
    correo varchar(100) unique,
    id_user int unique not null,
    foreign key (id_user) references usuarios(id_user) on delete restrict on update cascade
);
-- 4. Tabla de productos
create table productos(
	id_producto int auto_increment primary key,
    fecha_registro datetime default current_timestamp,
    producto varchar(100) unique not null,
    precio decimal(10,2) not null,
    stock int not null default 0
);
-- 5. Tabla de clientes
create table cliente(
	id_cliente int auto_increment primary key,
    fecha_registro datetime default current_timestamp,
    nombres varchar(100) not null,
    cedula varchar(20) unique not null,
    direccion text,
    telefono varchar(20),
    correo varchar(100) unique
); 
-- 6. Tabla de facturas (cabecera)
create table facturas(
	id_factura int auto_increment primary key,
	fecha_registro datetime default current_timestamp,
    id_cliente int not null,
    subtotal decimal(10,2) not null,
    IVA decimal(10,2) not null,
    total decimal(10,2) not null,
    foreign key (id_cliente) references cliente(id_cliente) on delete cascade on update cascade
);
-- 7. Tabla de detalle de facturas
create table detalle_factura(
	id_detalle int auto_increment primary key,
    id_factura int not null,
    id_producto int not null,
    cantidad int not null,
    precio_unitario decimal(10,2) not null,
    total_producto decimal(10,2) not null,
    foreign key (id_factura) references facturas(id_factura)  on delete cascade on update cascade,
    foreign key (id_producto) references productos(id_producto)  on delete cascade on update cascade
);


