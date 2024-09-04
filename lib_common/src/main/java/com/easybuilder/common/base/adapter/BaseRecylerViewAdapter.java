package com.easybuilder.common.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author renshijie
 * @email renshijie
 * @createTime 2024/1/11
 * @describe BaseRecylerViewAdapter
 **/
public abstract class BaseRecylerViewAdapter<T, VB extends ViewBinding>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_FOOTER = 2;

    private boolean headerView;
    private boolean footerView;

    private Context context;

    private OnItemClickListener<T> onItemClickListener;

    public BaseRecylerViewAdapter(Context context) {
        this.context = context;
    }

    private List<T> dataList;

    /**
     * 设置数据源
     *
     * @param dataList
     */
    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param dataList
     */
    public void addDataList(List<T> dataList) {
        if (this.dataList == null) {
            this.dataList = new ArrayList<>();
        }
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     * @param data
     */
    public void addData(T data){
        if (this.dataList == null)
            this.dataList = new ArrayList<>();
        this.dataList.add(data);
        notifyDataSetChanged();
    }

    public void setData(T data){
        if (this.dataList == null)
            this.dataList = new ArrayList<>();
        this.dataList.clear();
        this.dataList.add(data);
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clearDataList() {
        if (this.dataList != null) {
            this.dataList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 获取数据列表
     * @return
     */
    public List<T> getDataList() {
        return dataList;
    }

    /**
     * 更新数据
     *
     * @param data
     * @param position
     */
    public void updateItem(T data, int position) {
        if (this.dataList != null) {
            this.dataList.set(position, data);
            notifyItemChanged(position);
        }
    }

    /**
     * 移除数据
     *
     * @param position
     */
    public void removeItem(int position) {
        if (this.dataList != null) {
            this.dataList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount() - position);
        }
    }

    /**
     * 添加头部
     * 需要重写方法onCreateHeaderVieiwHolder
     *
     * @param flag
     */
    public void enableHeaderView(boolean flag) {
        if (headerView == flag) {
            return;
        }
        headerView = flag;
        notifyDataSetChanged();
    }

    /**
     * 添加底部
     * 需要重写方法onCreateFooterVieiwHolder
     *
     * @param flag
     */
    public void enableFooterView(boolean flag) {
        if (footerView == flag) {
            return;
        }
        footerView = flag;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return this.dataList.get(position).hashCode();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return onCreateHeaderVieiwHolder(parent, viewType);
        } else if (viewType == TYPE_FOOTER) {
            return onCreateFooterVieiwHolder(parent, viewType);
        } else {
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                Type[] params = ((ParameterizedType) type).getActualTypeArguments();
                Class clazz = (Class) params[1];
                Method inflate = null;
                try {
                    inflate = clazz.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
                    VB binding = (VB) inflate.invoke(null, LayoutInflater.from(parent.getContext())
                            , parent, false);
                    return new BaseViewHolder<VB>(binding);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        throw new RuntimeException("Binding view not found.");
    }

    protected RecyclerView.ViewHolder onCreateHeaderVieiwHolder(ViewGroup parent, int viewType) {
        return null;
    }

    protected RecyclerView.ViewHolder onCreateFooterVieiwHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            HeaderViewHolder tmpHoder = (HeaderViewHolder) holder;
            bindDataForHeader(tmpHoder);
            return;
        }
        if (getItemViewType(position) == TYPE_FOOTER) {
            FooterViewHolder tmpHoder = (FooterViewHolder) holder;
            bindDataForFooter(tmpHoder);
            return;
        }
        if (headerView) {
            position = position - 1;
        }
        if (dataList != null && !dataList.isEmpty()) {
            BaseViewHolder<VB> tmpHoder = (BaseViewHolder<VB>) holder;
            bindData(tmpHoder, dataList.get(position), position);
            int copyp = position;
            tmpHoder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (BaseRecylerViewAdapter.this.onItemClickListener != null) {
                        BaseRecylerViewAdapter.this.onItemClickListener.onItemClick(copyp, dataList.get(copyp));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int i = dataList != null ? dataList.size() : 0;
        return i + getHeaderCount() + getFooterCount();
    }

    public int getHeaderCount() {
        return !headerView ? 0 : 1;
    }

    public int getFooterCount() {
        return !footerView ? 0 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (headerView && position == 0) {
            return TYPE_HEADER;
        }
        if (footerView && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return 0;
    }

    public Context getContext() {
        return context;
    }

    public abstract void bindData(BaseViewHolder<VB> holder, T data, int position);

    protected void bindDataForHeader(HeaderViewHolder<? extends ViewBinding> holder) {
    }

    protected void bindDataForFooter(FooterViewHolder<? extends ViewBinding> holder) {
    }

    public static class BaseViewHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {
        private final VB binding;

        public BaseViewHolder(VB binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public VB getBinding() {
            return binding;
        }
    }

    public static class HeaderViewHolder<HB extends ViewBinding> extends RecyclerView.ViewHolder {
        private HB binding;

        public HeaderViewHolder(HB binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public HB getBinding() {
            return binding;
        }
    }

    public static class FooterViewHolder<FB extends ViewBinding> extends RecyclerView.ViewHolder {
        private final FB binding;

        public FooterViewHolder(FB binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public FB getBinding() {
            return binding;
        }
    }


    /**
     * 点击事件相关
     *
     * @param <T>
     */
    public interface OnItemClickListener<T> {
        void onItemClick(int position, T data);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void removeCallback() {
        if (this.onItemClickListener != null) {
            this.onItemClickListener = null;
        }
    }
}

/**
 * 示例
 * public class MyAdapter extends BaseRecyclerViewAdapter<String, ItemLayoutBinding> {
 * <p>
 * public MyAdapter(List<String> dataList) {
 * setDataList(dataList);
 * }
 *
 * @Override public void bindData(BaseViewHolder<ItemLayoutBinding> holder, String data, int position) {
 * holder.getBinding().tvTitle.setText(data);
 * }
 * <p>
 * }
 */