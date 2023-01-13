package com.compose.presentation.finalFragment

import java.io.File

sealed class FinalResultAction{
    object Start: FinalResultAction()
    object Error: FinalResultAction()
    object Loading: FinalResultAction()
    object Saved: FinalResultAction()
    object Plug: FinalResultAction()
    class SharedFile(val file: File): FinalResultAction()
    class SharedInstagram(val file: File): FinalResultAction()
    object SharedApp: FinalResultAction()
}
