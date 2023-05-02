package com.g2minus.chatapp

import java.net.URLEncoder

data class WalletProvider(val name: String, val image: Int, val link: (String) -> String)

val walletProviders = listOf(
    WalletProvider(
        name = "Metamask", image = R.drawable.metamask,
        link = { wc -> "https://metamask.app.link/wc?uri=${URLEncoder.encode(wc, "UTF-8")}" }
    ),
    WalletProvider(
        name = "Trust Wallet", image = R.drawable.trust_wallet,
        link = { wc -> "https://link.trustwallet.com/wc?uri=${URLEncoder.encode(wc, "UTF-8")}" }
    ),
    WalletProvider(
        name = "Ledger Live", image = R.drawable.ledger_wordmark_black_rgb,
        link = { wc -> "ledgerlive://wc?uri=${URLEncoder.encode(wc, "UTF-8")}" }
    ),
)

