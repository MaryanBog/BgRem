package com.compose.presentation.welcome

sealed class LinkAction{
    object Start: LinkAction()
    object PoliceLink: LinkAction()
    object EmailLink: LinkAction()
    object Terms: LinkAction()
    object Facebook: LinkAction()
    object Youtube: LinkAction()
    object Instagram: LinkAction()
}
