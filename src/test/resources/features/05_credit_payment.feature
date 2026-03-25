@regression @credit_payment
Feature: Pago de cuota de crédito

  Background:
    Given el usuario ha iniciado sesión

  Scenario: Pago total de cuota de crédito
    When navego a Operaciones
    And selecciono Pagar Crédito
    Then la pantalla de selección de crédito a pagar carga
    When selecciono Paga un crédito propio
    Then los créditos cargan correctamente
    When selecciono el crédito número 2
    Then el menú del crédito carga
    When selecciono Pago de cuota
    Then la pantalla de pago de cuota carga
    When confirmo el detalle de pago
    Then la pantalla de detalles de pago carga
    When realizo el pago de la cuota
    Then el comprobante de pago exitoso aparece
