# BottomDialog
BottomDialog 一个自定义的从底部弹出的dialog，仿照微博的分享弹框

# 效果图 
![Alt text](/art/horizontal.png) ![Alt text](/art/vertical.png)

# 导入
    compile 'top.zibin:BottomDialog:1.0.5'

#Release Notes
###[v1.0.5, 2016/10/21](https://github.com/Curzibn/BottomDialog/milestone/1)

- 修改多行显示下只有最后一行点击有相应问题

# 使用
BottomDialog 只需要一句代码即可轻松显示底部弹框

## 创建Menu
    <?xml version="1.0" encoding="utf-8"?>
    <menu xmlns:android="http://schemas.android.com/apk/res/android">
        <item
            android:id="@+id/moments"
            android:icon="@mipmap/ic_share_moments"
            android:title="@string/moments" />
    </menu>
    
## 设置 Layout 样式

    new BottomDialog(MainActivity.this)
        .title(R.string.title_item)             //设置标题
        .layout(BottomDialog.GRID)              //设置内容layout,默认为线性(LinearLayout)
        .orientation(BottomDialog.VERTICAL)     //设置滑动方向,默认为横向
        .inflateMenu(R.menu.menu_share)         //传人菜单内容
        .itemClick(new OnItemClickListener() {  //设置监听
            @Override
            public void click(Item item) {
                Toast.makeText(MainActivity.this, getString(R.string.share_title) + item.getTitle(), Toast.LENGTH_LONG).show();
            }
        })
        .show();

## Listener 事件监听方式调用
    new BottomDialog(context)
        .title(R.string.share_title)
        .inflateMenu(R.menu.menu_share)
        .itemClick(new OnItemClickListener() {
            @Override
            public void click(Item item) {
                    //TODO 处理点击结果
                }
            })
        .show();
