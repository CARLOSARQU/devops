# language: es
Característica: Pago de cuota de crédito

  Antecedentes:
    Dado el usuario ha iniciado sesión

  Escenario: Pago total de cuota de crédito
    Cuando navego a Operaciones
    Y selecciono Pagar Crédito
    Entonces la pantalla de selección de crédito a pagar carga
    Cuando selecciono Paga un crédito propio
    Entonces los créditos cargan correctamente
    Cuando selecciono el primer crédito
    Entonces el menú del crédito carga
    Cuando selecciono Pago de cuota
    Entonces la pantalla de pago de cuota carga
    Cuando confirmo el detalle de pago
    Entonces la pantalla de detalles de pago carga
    Cuando realizo el pago de la cuota
    Entonces el comprobante de pago exitoso aparece
