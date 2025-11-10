-- 										=== DATABASE
DROP DATABASE IF EXISTS viajes_bus;
CREATE DATABASE viajes_bus;
USE viajes_bus;
-- 										=== TABLES
CREATE TABLE viajes(
	codigo INT AUTO_INCREMENT PRIMARY KEY,
    destino VARCHAR(100) NOT NULL,
    plazas_disponibles INT UNSIGNED DEFAULT 0    
);
CREATE TABLE clientes(
	codigo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);
CREATE TABLE reservas(
	numero_reserva INT AUTO_INCREMENT PRIMARY KEY,
    codigo_viaje INT,
    codigo_cliente INT,
    plazas_reservadas INT UNSIGNED DEFAULT 0,
    estado ENUM("A","E","C") DEFAULT "E",
    FOREIGN KEY (codigo_viaje) REFERENCES viajes(codigo),
    FOREIGN KEY (codigo_cliente) REFERENCES clientes(codigo)
);
-- 										=== INSERT
INSERT INTO viajes VALUES(NULL, "Madrid",50),(NULL, "Francia",100),(NULL, "Italia",200);
INSERT INTO clientes VALUES(NULL, "Antonio"),(NULL, "María"),(NULL, "Carlos");
-- 										=== PROCEDIMIENTOS
DELIMITER //
CREATE PROCEDURE cancelar_reserva (IN num INT, IN codCli INT)
	BEGIN
	DECLARE codViaje INT;
	DECLARE plazas INT;
	DECLARE estadoActual CHAR(1);
	SELECT codigo_viaje, plazas_reservadas, estado INTO codViaje, plazas, estadoActual
		FROM reservas
		WHERE numero_reserva = num AND codigo_cliente = codCli;
	UPDATE reservas
		SET estado = 'C'
		WHERE numero_reserva = num AND codigo_cliente = codCli;
END//
DELIMITER ;
-- 										=== TRIGGERS
DELIMITER $$
CREATE TRIGGER liberar_plazas_canceladas
	AFTER UPDATE ON reservas
	FOR EACH ROW
BEGIN
	IF NEW.estado = 'C' AND OLD.estado = 'A' THEN
		UPDATE viajes
			SET plazas_disponibles = plazas_disponibles + OLD.plazas_reservadas
			WHERE codigo = OLD.codigo_viaje;
	END IF;
END$$

CREATE TRIGGER plazas_insertadas
	AFTER INSERT ON reservas
    FOR EACH ROW
BEGIN
	IF NEW.estado = "A" THEN
		UPDATE viajes
			SET plazas_disponibles = plazas_disponibles - NEW.plazas_reservadas
            WHERE codigo = NEW.codigo_viaje;
	END IF;
END$$
DELIMITER ;




