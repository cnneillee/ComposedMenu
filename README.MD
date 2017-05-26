# ComposedMenu
[中文][1] | [English][2]
## Introduction
An android library makes menu awesome!

## Including
Currently, there are two customized Menu:
- `RotatingArcMenu`
- `LinearFlickMenu`

## Usage
For example, here comes the usage of `RotatingArcMenu`!

Define in `xml` file:
```
<cn.neillee.composedmenu.RotatingArcMenu
    android:id="@+id/ram1"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:position="left_top"
    app:radius="150dp"/>
```

Configure in `java`:
```
RotatingArcMenu ram1 = (RotatingArcMenu) findViewById(R.id.ram1);
ram1.setCenterButtonIcon(R.drawable.composer_icn_plus);
ram1.addItem(new RotatingArcMenu.MenuItem(ram1)
                .setIcon(R.drawable.composer_music), 0);
ram1.addItem(new RotatingArcMenu.MenuItem(ram1)
                .setIcon(R.drawable.composer_camera), 1);
ram1.addItem(new RotatingArcMenu.MenuItem(ram1)
                .setIcon(R.drawable.composer_thought), 2);
```

Add Listener:
```
ram1.setOnMenuItemClickListener(new ComposedMenu.OnMenuItemClickListener() {
    @Override
    public void onMenuItemClick(ViewGroup parent, View v, int pos) {
        String info = "RAM1-" + pos + "Clicked!";
        Toast.makeText(this, info, android.widget.Toast.LENGTH_SHORT).show();
    }
});

ram1.setOnMenuStatusChangedListener(new OnMenuStatusChangedListener() {
    @Override
    public void onStatusChanged(ViewGroup parent, ComposedMenu.Status from, ComposedMenu.Status to) {
        String info = "RAM1-" + to.name();
        Toast.makeText(this, info, android.widget.Toast.LENGTH_SHORT).show();
    }
});
```

[1]: README_CN.MD
[2]: README.MD