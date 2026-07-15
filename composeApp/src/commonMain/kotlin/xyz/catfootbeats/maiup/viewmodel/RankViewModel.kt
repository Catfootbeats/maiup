package xyz.catfootbeats.maiup.viewmodel

import androidx.lifecycle.ViewModel
import xyz.catfootbeats.maiup.api.LxnsApi

/**
 * TODO: 排行榜功能预留，待实现。
 * 移除 init 自动加载和 handleApiCall 重复代码，用 ApiCallHandler 代替。
 */
class RankViewModel(
    private val lxnsApi: LxnsApi,
) : ViewModel()
