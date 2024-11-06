package com.easybuilder.base.bean

import android.app.Activity

data class PageBean(var cls:Class<out Activity>,var title:String,val imageId:Int)
