# language: es
@regression @own_transfer
Característica: Transferencia entre cuentas propias

  Antecedentes:
    Dado el usuario ha iniciado sesión

  Escenario: Transferencia entre mis cuentas por S/ 1.00
    Cuando navego a Operaciones
    Y selecciono Transferencias
    Y selecciono Entre mis cuentas
    Entonces la pantalla de detalles de transferencia carga
    Cuando ingreso el monto "100" y continúo
    Entonces la pantalla de resumen de transferencia carga
    Cuando confirmo la transferencia
    Entonces el comprobante de transferencia exitosa aparece
