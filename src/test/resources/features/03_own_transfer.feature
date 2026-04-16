@regression @own_transfer
Feature: Transferencia entre cuentas propias

  Background:
    Given el usuario ha iniciado sesión

  Scenario: Transferencia exitosa entre mis cuentas por S/ 1.00
    When navego a Operaciones
    And selecciono Transferencias
    And selecciono Entre mis cuentas
    Then la pantalla de detalles de transferencia carga
    When ingreso el monto "100" y continúo
    Then la pantalla de resumen de transferencia carga
    When confirmo la transferencia
    Then el comprobante de transferencia exitosa aparece

  Scenario Outline: Validación de reglas de negocio en el monto de transferencia
    When navego a Operaciones
    And selecciono Transferencias
    And selecciono Entre mis cuentas
    Then la pantalla de detalles de transferencia carga
    When ingreso el monto invalido "<monto>"
    Then aparece el mensaje de validación "<mensaje_esperado>"

    Examples:
      | monto    | mensaje_esperado                               |
      | 0        | El monto mínimo es de                          |
      | 3000001 | El monto excede el límite de transferencia      |
