package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.Pojo.Book_pojo;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;
import erp.skoolerp.utils.Volley_simpleapi;

/**
 * Created by shalu on 21/06/17.
 */

public class Book_request extends Fragment
{
    View view;
    Spinner category, book;
    Button request;
    ArrayList<String> cat_array, book_array, catid_Array;
    ArrayAdapter<String> cat_adap, book_adap;
    Book_pojo pojo;
    ArrayList<Book_pojo> book_data;
    String cat_id, lib_id;
    TextView isbn, author, duedate, numbr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.book_request, container, false);

        category = (Spinner) view.findViewById(R.id.categry_list);
        book = (Spinner) view.findViewById(R.id.book_list);
        request = (Button) view.findViewById(R.id.request);
        isbn = (TextView) view.findViewById(R.id.isbn);
        author = (TextView) view.findViewById(R.id.author);
        duedate = (TextView) view.findViewById(R.id.duedate);
        numbr = (TextView) view.findViewById(R.id.numbr);

        cat_array = new ArrayList<>();
        book_array = new ArrayList<>();
        catid_Array = new ArrayList<>();
        book_data = new ArrayList<>();

        request.setTypeface(Wschool.tf3);

        cat_array.add("Select category");
        cat_adap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_lightblack, cat_array);
        cat_adap.setDropDownViewResource(R.layout.spinner_drop_item);
        category.setAdapter(cat_adap);

        book_array.add("Select book");

        book_adap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_lightblack, book_array);
        book_adap.setDropDownViewResource(R.layout.spinner_drop_item);
        book.setAdapter(book_adap);

        category_Listing();

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                cat_id = "";
                if(catid_Array != null && i!=0)
                {
                    cat_id = catid_Array.get(i-1);
                    booklisting();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        book.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                lib_id = "";
                if(book_data != null && i!=0)
                {
                    lib_id = book_data.get(i-1).getId();
                    isbn.setText("ISBN Number: "+book_data.get(i-1).getIsbn());
                    author.setText("Author name: "+book_data.get(i-1).getAuthor());
                    duedate.setText("Due date: "+book_data.get(i-1).getDuedate());
                    numbr.setText("Book number: "+book_data.get(i-1).getBooknmb());
                }else
                {
                    isbn.setText("ISBN Number: ");
                    author.setText("Author name: ");
                    duedate.setText("Due date: ");
                    numbr.setText("Book number: ");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!cat_id.equals("") && !lib_id.equals(""))
                {
                   request_book();
                }else {

                    Toast.makeText(getActivity(), "Choose your book details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void request_book() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        params.put("libraryid", lib_id);
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "bookrequest";
        new Volley_simpleapi(getActivity(), Book_request.this, url, params, new Volley_simpleapi.Contents() {
            @Override
            public void returndata(boolean s) {

                if(s){
                    Toast.makeText(getActivity(), "Book request sent.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Request failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void booklisting() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        params.put("bookcategoryid", cat_id);
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "booklistcategorywise";
        new Volley_load(getActivity(), Book_request.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s) {

                book_data = new ArrayList<>();
                int len = s.length();
                if(len > 0){
                    for (int i = 0; i < len; i++) {
                        try
                        {
                            JSONObject jo1 = s.getJSONObject(i);
                            pojo = new Book_pojo();
                            pojo.setId(jo1.getString("libraryid"));
                            pojo.setAuthor(jo1.getString("librarybooks_author"));
                            pojo.setIsbn(jo1.getString("librarybooks_isbn"));
                            pojo.setDuedate(jo1.getString("due_date"));
                            pojo.setBookname(jo1.getString("librarybooks_title"));
                            pojo.setBooknmb(jo1.getString("librarybooks_lbookno"));
                            book_array.add(jo1.getString("librarybooks_title"));
                            book_data.add(pojo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    book_adap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_lightblack, book_array);
                    book_adap.setDropDownViewResource(R.layout.spinner_drop_item);
                    book.setAdapter(book_adap);
                }else{
                    Toast.makeText(getActivity(), "No book found.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void category_Listing() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "bookcategorylist";
        new Volley_load(getActivity(), Book_request.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s) {
                int len = s.length();
                if(len > 0){
                    for (int i = 0; i < s.length(); i++) {
                        try
                        {
                            JSONObject jo1 = s.getJSONObject(i);
                            cat_array.add(jo1.getString("bookcategory_name"));
                            catid_Array.add(jo1.getString("bookcategoryid"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    cat_adap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_lightblack, cat_array);
                    cat_adap.setDropDownViewResource(R.layout.spinner_drop_item);
                    category.setAdapter(cat_adap);
                }else{
                    Toast.makeText(getActivity(), "Categories unavailable.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
