package com.dwm.winesearchapp_extern;

import com.dwm.winesearchapp_extern.Pojo.request.FacetQueryGroup;

import java.util.ArrayList;
import java.util.List;

public class Session {
        private static int _maxWinesSearch;
        private static int _winesPerPage = 10;
        private static String _wineName = null;
        private static List<FacetQueryGroup> _facetQueryGroups = new ArrayList<>();
        private static WineListItem _selectedListItem;


        public static WineListItem getSelectedListItem() {
                return _selectedListItem;
        }

        public static void setSelectedListItem(WineListItem selectedListItem) {
                _selectedListItem = selectedListItem;
        }

        public static int getMaxWinesSearch() {
                return _maxWinesSearch;
        }

        public static void setMaxWinesSearch(int maxWinesSearch) {
                _maxWinesSearch = maxWinesSearch;
        }

        public static boolean allWinesLoaded(int loaded) {
                return loaded == _maxWinesSearch;
        }

        public static int getWinesPerPage() {
                return _winesPerPage;
        }

        public static void setWinesPerPage(int winesPerPage) {
                _winesPerPage = winesPerPage;
        }

        public static String getWineName() {
                return _wineName;
        }

        public static void setWineName(String wineName) {
                _wineName = wineName;
        }

        public static List<FacetQueryGroup> getFacetQueryGroups() {
                return _facetQueryGroups;
        }

        public static void addFacetQueryGroupValue(String name, String value) {
                for (FacetQueryGroup group : _facetQueryGroups) {
                        if (group.fieldName.equals(name)) {
                                group.values.add(value);
                                return;
                        }
                }
                _facetQueryGroups.add(new FacetQueryGroup(name, value));
        }

        public static void removeFacetQueryGroupValue(String name, String value) {
                for (FacetQueryGroup group : _facetQueryGroups) {
                        if (group.fieldName.equals(name)) {
                                group.values.remove(value);
                                if (group.values.size() == 0) {
                                        _facetQueryGroups.remove(group);
                                }
                                return;
                        }
                }
        }

        public static void setFacetQueryGroups(List<FacetQueryGroup> facetQueryGroups) {
                _facetQueryGroups = facetQueryGroups;
        }
}
