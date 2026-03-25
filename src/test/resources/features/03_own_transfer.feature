@regression @own_transfer
Feature: Transferencia entre cuentas propias

  Background:
    Given el usuario ha iniciado sesión

  Scenario: Transferencia entre mis cuentas por S/ 1.00
    When navego a Operaciones
    And selecciono Transferencias
    And selecciono Entre mis cuentas
    Then la pantalla de detalles de transferencia carga
    When ingreso el monto "100" y continúo
    Then la pantalla de resumen de transferencia carga
    When confirmo la transferencia
    Then el comprobante de transferencia exitosa aparece
