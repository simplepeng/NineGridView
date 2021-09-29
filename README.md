# NineGridView

可能是最通用的九宫格布局

* 正常九宫格视图，仿微信，Item显示样式可完全自定义
* 超过9个Item，可自定义额外展示的布局
* 自定义适配单个item视图显示样式
* 自定义item间的间距
* 适配4个item时的显示样式，仿B站

| 默认                 | 单个视图             | 超过预设数量         | 适配4个item          |
| -------------------- | -------------------- | -------------------- | -------------------- |
| ![](files/img_1.jpg) | ![](files/img_2.jpg) | ![](files/img_3.jpg) | ![](files/img_4.jpg) |

## 依赖

[![](https://jitpack.io/v/simplepeng/NineGridView.svg)](https://jitpack.io/#simplepeng/NineGridView)

```groovy
maven { url 'https://jitpack.io' }
```

```groovy
dependencies {
    implementation 'com.github.simplepeng:NineGridView:v1.0.1'
}
```

## 使用

继承`NineGridView.Adapter()`，重写需要的方法，具体可查看demo中的使用方法。

```kotlin
class CustomAdapter : NineGridView.Adapter()
```

```kotlin
abstract class Adapter {

    // 返回总的item数量
    abstract fun getItemCount(): Int

    //分发各种不同itemType，类似RecyclerView
    fun getItemViewType(position: Int) = 0

    //适配4个itemView的样式-类似哔哩哔哩
    open fun adaptFourItem(): Boolean = false

    //默认的ItemView样式
    abstract fun onCreateItemView(parent: ViewGroup, viewType: Int): View

    abstract fun onBindItemView(itemView: View, position: Int)

    //是否适配单个ItemView的样式
    open fun adaptSingleView(): Boolean = false

    open fun onCreateSingleView(parent: ViewGroup, viewType: Int): View? = null

    open fun onBindSingleView(singleView: View, position: Int) {

    }

    //是否适配额外的View的样式
    open fun enableExtraView(): Boolean = false

    open fun onCreateExtraView(parent: ViewGroup, viewType: Int): View? = null

    open fun onBindExtraView(extraView: View, position: Int) {

    }
}
```

可使用的属性

```xml
app:ngv_spanCount		//横向的item数量，默认为3
app:ngv_itemGap			//item间的间距，默认为1dp
app:ngv_maxCount		//最多显示的item数量，默认为9
```

如果不需要自定义的ItemView，也可以直接使用本库封装好的`ImageAdapter`，效果就是预览图那种。

```kotlin
val imageAdapter = ImageAdapter(item.images, onBindView = { imageView, item, position ->
    Glide.with(imageView)
        .load(item)
        .centerCrop()
        .into(imageView)
})
imageAdapter.onItemViewClick = { item, position ->
    toast("ItemView click -- $position")
}
imageAdapter.onExtraViewClick = { position ->
    toast("ExtraView click  $position")
}
holder.nineGridView.adapter = imageAdapter
```

## 版本迭代

* v1.0.1：
  * 修改属性名，预防和其他View的属性冲突
  * 增加`ImageAdapter`
  * 去掉`BuildConfig.java`
  * 增加`isInEditMode`，在布局编辑状态也可以预览样式
* v1.0.0：初次上传

