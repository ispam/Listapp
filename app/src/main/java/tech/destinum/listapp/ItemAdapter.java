package tech.destinum.listapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private ArrayList<ItemClass> itemList;
    private final DBHelper dbHelper;

    public ItemAdapter(Context context, DBHelper dbHelper, ArrayList<ItemClass> itemList) {
        this.itemList = itemList;
        this.layoutInflater = LayoutInflater.from(context);
        this.dbHelper = dbHelper;
    }

    @Override
    public int getCount() {
        return itemList != null ? itemList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return itemList.get(i)._id;
    }

    //use this function to assign list of items
    public void setItemList(ArrayList<ItemClass> itemList) {
        this.itemList.clear();
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ItemClass itemClass = itemList.get(position);

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItemH(itemClass);
            }
        });
        viewHolder.tv_item.setText(itemClass.detail);
        return convertView;
    }

    private void deleteItemH(ItemClass item) {
        dbHelper.deleteItemById(item._id);
        setItemList(dbHelper.getItemList());
    }

    private static class ViewHolder {
        final Button btn_delete;
        final TextView tv_item;

        ViewHolder(View view) {
            btn_delete = (Button) view.findViewById(R.id.btnDelete);
            tv_item = (TextView) view.findViewById(R.id.item_name);
        }
    }
}
