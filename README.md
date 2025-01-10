# kotlin_common

#### 介绍
方便kotlin快速开发框架
#### 软件架构
软件架构说明


#### 添加依赖
[![](https://jitpack.io/v/Njfk/kotlin_common.svg)](https://jitpack.io/#Njfk/kotlin_common)

####功能说明  
1.网络请求  
2.线程池  
3.MVVM基类  
4.权限请求
5.RecyclerView适配器支持添加头尾

####使用说明
1.网络请求
```
interface ApiService {
    @GET("/FRwK4ja2fc753bd4e3704d2b368b5bbe0104e39e0835f1c/bn/api/1.0/auth/oauth/token?branchID=0")
    suspend fun test(): Response<TestBean<Value>>

    @GET("/FRwK4ja2fc753bd4e3704d2b368b5bbe0104e39e0835f1c/bn/api/1.0/auth/oauth/token?branchID=0")
    fun test2(): Call<TestBean<Value>>
}

class SampleRepository{
    val client: ApiService by lazy {
        RetrofitClient.createService<ApiService>("https://mockapi.eolink.com/")
    }
    //方式1
    suspend fun test(): Response<TestBean<Value>>? {
        return client.test()
    }
    //方式2
    suspend fun test2(onSuccess:(data:Response<TestBean<Value>>?)->Unit={}, onError:(e:Exception)->Unit={}): Unit{
        try {
            onSuccess(client.test())
        }catch (e:Exception){
            onError(e)
        }
    }
    //方式3
    fun test3(): Call<TestBean<Value>> {
        return client.test2()
    }

   suspend fun test4(callback:INetCallback<Response<TestBean<Value>>>): Unit {
        try {
            callback.onSuccess(client.test())
        }catch (e:Exception){
            callback.onFailed(e)
        }
    }
}
```  
2.线程池
```
   ThreadUtil.getInstance().addTask(object : ThreadTask() {
            override fun runTask() {
                "${Thread.currentThread().id}".log("===")
            }
        });

```  
3.MVVM基类
```

class PagingLibraryActivity : BaseVMActivity<ActivityPagingBinding, PagingViewModel>(
    PagingViewModel::class.java
) {
    private lateinit var adapter: ItemAdapter

    override suspend fun observe() {

    }

    override fun initView() {
        
    }

    override fun loadData() {
       
    }
}
```

4.权限请求
```
PermissionHelper.with(getActivity()).requestPermission(new PermissionHelper.PermissionCallback() {
            @Override
            public void callback(Map<String, Boolean> granted, Map<String, Boolean> denied, boolean isAllGranted, boolean isAllDenied) {
                if (granted != null && granted.containsKey(Manifest.permission.ACCESS_COARSE_LOCATION) && granted.containsKey(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //dosomething
                }
            }

            @Override
            public void callback(Boolean result) {

            }
        }, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        });

```
5.RecyclerView适配器支持添加头尾
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="15dp">

    <TextView
        android:id="@+id/title"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="20sp"/>

    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="#ccc"/>
</LinearLayout>


class ItemCustomApdater(contxt: Context): BaseRecyclerViewAdapterKt<String, ItemPageBinding>(contxt) {
    override fun bindData(
        holder: com.easybuilder.common.base.adapter.BaseViewHolder<ItemPageBinding>?,
        data: String,
        position: Int
    ) {
        holder?.binding?.title?.text = data
    }

    override fun bindMultiTypeData(holder: RecyclerView.ViewHolder?, t: String, position: Int) {
        TODO("Not yet implemented")
    }

    override fun bindDataForHeader(holder: HeaderViewHolder<out ViewBinding?>?) {
        super.bindDataForHeader(holder)
        var h = holder as HeaderViewHolder<ItemPageBinding>
        h?.binding?.title?.text = "头部"
    }


    override fun bindDataForFooter(holder: FooterViewHolder<out ViewBinding?>?) {
        super.bindDataForFooter(holder)
        var h = holder as FooterViewHolder<ItemPageBinding>
        h?.binding?.title?.text = "底部"
    }

    override fun onCreateHeaderVieiwHolder(
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder? {
        val inflate = ItemPageBinding.inflate(LayoutInflater.from(getContext()))
        return HeaderViewHolder(inflate)
    }

    override fun onCreateFooterVieiwHolder(
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder? {
        val inflate = ItemPageBinding.inflate(LayoutInflater.from(getContext()))
        return FooterViewHolder(inflate)
    }
}


class BaseRvAdapterKtActivity : BaseVMActivity<ActivityPagingBinding, BaseRvAdapterViewModel>(
    BaseRvAdapterViewModel::class.java
) {
    private lateinit var adapter: ItemCustomApdater

    override suspend fun observe() {

    }

    override fun initView() {
        adapter = ItemCustomApdater(this)
        adapter.enableFooterView(true)
        adapter.enableHeaderView(true)
        mBinding.rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mBinding.rv.adapter = adapter

        adapter.setDataList(mutableListOf("1", "2", "3", "4", "5"))
        adapter.onItemClickListener = object : OnItemClickListener<String> {
            override fun onItemClick(position: Int, data: String) {
                data.log("pager")
            }
        }

    }

    override fun loadData() {

    }
}
```