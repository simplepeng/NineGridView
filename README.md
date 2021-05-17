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

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

```groovy
dependencies {
    implementation 'com.github.simplepeng:NineGridView:v1.0.0'
}
```

## 使用

继承`NineGridView.Adapter()`，重写需要的方法，具体可查看demo中的使用方法。

```kotlin
class Adapter : NineGridView.Adapter()
```

可使用的属性

```xml
app:spanCount//横向的item数量，默认为3
app:itemGap//item间的间距，默认为1dp
app:maxCount//最多显示的item数量，默认为9
```

## 版本迭代

* v1.0.0：初次上传

