package me.curzbin.bottomdialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import me.curzbin.library.BottomDialog;
import me.curzbin.library.Item;
import me.curzbin.library.OnItemClickListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.horizontal_single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new BottomDialog(MainActivity.this)
                        .title(R.string.share_title)
                        .orientation(BottomDialog.HORIZONTAL)
                        .inflateMenu(R.menu.menu_share, new OnItemClickListener() {
                            @Override
                            public void click(Item item) {
                                Toast.makeText(MainActivity.this, getString(R.string.share_title) + item.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        findViewById(R.id.horizontal_multi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new BottomDialog(MainActivity.this)
                        .title(R.string.share_title)
                        .orientation(BottomDialog.HORIZONTAL)
                        .inflateMenu(R.menu.menu_share, new OnItemClickListener() {
                            @Override
                            public void click(Item item) {
                                Toast.makeText(MainActivity.this, getString(R.string.share_title) + item.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .inflateMenu(R.menu.menu_main, new OnItemClickListener() {
                            @Override
                            public void click(Item item) {
                                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        findViewById(R.id.vertical).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new BottomDialog(MainActivity.this)
                        .title(R.string.title_item)
                        .orientation(BottomDialog.VERTICAL)
                        .inflateMenu(R.menu.menu_share, new OnItemClickListener() {
                            @Override
                            public void click(Item item) {
                                Toast.makeText(MainActivity.this, getString(R.string.share_title) + item.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        findViewById(R.id.grid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new BottomDialog(MainActivity.this)
                        .title(R.string.title_item)
                        .layout(BottomDialog.GRID)
                        .orientation(BottomDialog.VERTICAL)
                        .inflateMenu(R.menu.menu_grid, new OnItemClickListener() {
                            @Override
                            public void click(Item item) {
                                Toast.makeText(MainActivity.this, getString(R.string.share_title) + item.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
    }
}
