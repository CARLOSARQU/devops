# language: es
Característica: Transferencia a otras cuentas Los Andes

  Antecedentes:
    Dado el usuario ha iniciado sesión

  Escenario: Transferencia a otra cuenta Los Andes por S/ 1.00
    Cuando navego a Operaciones
    Y selecciono Transferencias
    Y selecciono A otras cuentas Los Andes
    Entonces la pantalla de ingreso de cuenta carga
    Cuando ingreso el número de cuenta destino y continúo
    Entonces la pantalla de detalles carga
    Cuando ingreso el monto "100" en transferencia a terceros y continúo
    Entonces la pantalla de resumen de transferencia a terceros carga
    Cuando confirmo la transferencia a terceros
    Entonces la pantalla OTP aparece
    Cuando envío el OTP
    Entonces el comprobante de transferencia a terceros aparece
