package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

public enum OperationType {
    CREATE_BALANCE, //	Utworzenie rachunku.
    ADD_FUNDS, //	Wpłata na rachunek.
    SUBTRACT_FUNDS, //	Wypłata z rachunku.
    TRANSFER_FROM, //	Przelew wewnętrzny z rachunku.
    TRANSFER_TO, //	Przelew wewnętrzny na rachunek.
    TRANSACTION_COMMISSION_OUTCOME, //	Pobranie prowizji za transakcję.
    TRANSACTION_PRE_LOCKING, //	Blokada środków na rachunku.
    LOCK_FUNDS, //	Blokada środków na rachunku.
    ADMIN_LOCK_FUNDS, //	Blokada środków na rachunku
    UNLOCK_FUNDS, //	Odblokowanie środków na rachunku.
    ADMIN_UNLOCK_FUNDS, //	Odblokowanie środków na rachunku.
    TRANSACTION_POST_INCOME, //	Otrzymanie środków z transakcji na rachunek.
    TRANSACTION_POST_OUTCOME, //	Pobranie środków z transakcji z rachunku
    ADMIN_ADD_FUNDS, //	Korekta środków na rachunku
    ADMIN_SUBTRACT_FUNDS, //	Korekta środków na rachunku
    TRANSACTION_OFFER_ABORTED_RETURN, //	Zwrot za usunięcie oferty na rachunek
    AFFILIATE_FUNDS_TRANSFER, //	Transfer środków z programu partnerskiego
    PAY_WITHDRAW_LOCK, //	Blokada środków na rachunku: {name}
    TRANSACTION_OFFER_COMPLETED_RETURN, //	Anulowanie oferty poniżej wartości minimalnych
    TRANSACTION_FAIL_UNLOCKING, //	Zwrot po nieudanej próbie wystawienia oferty
    WITHDRAWAL_LOCK_FUNDS, //	Blokada środków
    WITHDRAWAL_UNLOCK_FUNDS, //	Odblokowanie środków
    WITHDRAWAL_SUBTRACT_FUNDS, //	Wypłata środków
    FUNDS_MIGRATION, //	Migracja środków
    TRANSACTION_COMMISSION_RETURN, //	Zwrot prowizji
    ADD_FUNDS_ERROR_REFUND, //	Korekta blokady
    OLD_ADDRESS_CRYPTO_DEPOSIT, //	Wpłata na stary adres
    FIAT_DEPOSIT_MIGRATED, //	Wpłata na rachunek: {name}
    ADMIN_WITHDRAWAL_REFUND, //	Zwrot środków za anulowaną wypłatę
    SUBTRACT_FUNDS_ERROR_CORRECTION, //	Korekta środków
    ADD_FUNDS_COMPENSATION, //	Rekompensata
    BITCOIN_GOLD_FORK, //	Fork Bitcoin
    BALANCE_DEPOSIT_CORRECTION, //	Korekta wpłaty
    WITHDRAWAL_CUSTOM_COMMISSION, //	Prowizja za przelew dla sieci Bitcoin}
    CARD_ADMISSION, //	Zasilenie karty z rachunku
    CARD_ORDER, //	Zamówienie karty
    INCOME, //	Zasilenie karty
    OUTCOME, //	Wypłata z karty
    LOCK_CARD_ADMISSION, //	Zasilenie karty, blokada środków
    UNLOCK_CARD_ADMISSION, //	Anulowanie zasilenia karty
    SUBTRACT_CARD_ADMISSION, //	Zasilenie karty
    LOCK_ORDER_COMISSION, //	Blokada środków za zamówienie karty
    SUBTRACT_CARD_ORDER, //	Opłata za wydanie karty
    UNLOCK_CARD_ORDER, //	Odblokowanie środków za wydanie karty
}
