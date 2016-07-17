# BottomDialog
BottomDialog 一个自定义的从底部弹出的dialog，仿照微博的分享弹框

# 效果图
![Alt text](/art/demo.gif)

# 使用
BottomDialog 只需要一句代码即可轻松显示底部弹框

## Listener 方式
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
        
## RxBus 方式
    RxBus rxBus = new RxBus();
    
    rxBus.toObservable()
            .subscribe(new Action1<Object>() {
                @Override
                public void call(Object event) {
                    if (event instanceof Item)
                        //TODO 处理点击结果
                }
            });
            
    new BottomDialog(MainActivity.this)
        .title(R.string.share_title)
        .inflateMenu(R.menu.menu_share)
        .itemClick(rxBus)
        .show();
    
