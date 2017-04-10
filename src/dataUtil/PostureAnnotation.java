/*
 * Copyright (C) 2017 MSHAO1
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dataUtil;

import java.util.ArrayList;

/**
 *
 * @author MSHAO1
 */
public class PostureAnnotation {
    private String title = "";
    private ArrayList<String> value = new ArrayList();
    
    public PostureAnnotation(String title, ArrayList<String> value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getValue() {
        return value;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setValue(ArrayList<String> value) {
        this.value = value;
    }
    
}
