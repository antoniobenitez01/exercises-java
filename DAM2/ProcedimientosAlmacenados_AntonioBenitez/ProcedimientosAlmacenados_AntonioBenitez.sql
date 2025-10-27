USE almacenados;
DROP TABLE IF EXISTS usuarios;
CREATE TABLE usuarios(
	coduser INT AUTO_INCREMENT PRIMARY KEY,
    nombrelogin VARCHAR(30),
    contrasena VARCHAR(50),
    nombrecompleto VARCHAR(40),
    entradas INT
);

DROP PROCEDURE IF EXISTS insertarUsuario;
DELIMITER //
CREATE PROCEDURE insertarUsuario(nombrep VARCHAR(12),contrasenap VARCHAR(16),nombrecomp VARCHAR(40))
BEGIN
	INSERT INTO usuarios VALUES (NULL,nombrep,MD5(contrasenap),nombrecomp, 0);
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS getEntradas;
DELIMITER //
CREATE PROCEDURE getEntradas(IN nombrep VARCHAR(12), OUT numEntradas INT UNSIGNED)
BEGIN
	IF (SELECT COUNT(*) FROM usuarios WHERE nombrelogin = nombrep) > 0 THEN
		SELECT entradas INTO numEntradas FROM usuarios WHERE nombrelogin = nombrep;
	ELSE
		SELECT "-1" INTO numEntradas;
    END IF;
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS userLogin;
DELIMITER //
CREATE PROCEDURE userLogin(IN nombrep VARCHAR(12), IN contrasenap VARCHAR(16), OUT resultado VARCHAR(50))
BEGIN
	IF (SELECT COUNT(*) FROM usuarios WHERE nombrelogin = nombrep) > 0 THEN
		IF (SELECT contrasena FROM usuarios WHERE nombrelogin = nombrep) = MD5(contrasenap) THEN
			SET @usercompleto := (SELECT nombrecompleto FROM usuarios WHERE nombrelogin = nombrep);
			SET resultado = CONCAT("Login correcto, bievenid@ ",@usercompleto,".");
            UPDATE usuarios SET entradas = entradas + 1 WHERE nombrelogin = nombrep;
		ELSE
			SET resultado = "La contraseña introducida no es correcta.";
		END IF;
    ELSE
		SET resultado = "El usuario introducido no está registrado.";
    END IF;
END //
DELIMITER ;