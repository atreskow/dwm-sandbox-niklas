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


        public static WineListItem GetSelectedListItem() {
                return _selectedListItem;
        }

        public static void SetSelectedListItem(WineListItem selectedListItem) {
                _selectedListItem = selectedListItem;
        }

        public static int GetMaxWinesSearch() {
                return _maxWinesSearch;
        }

        public static void SetMaxWinesSearch(int maxWinesSearch) {
                _maxWinesSearch = maxWinesSearch;
        }

        public static boolean AllWinesLoaded(int loaded) {
                return loaded == _maxWinesSearch;
        }

        public static int GetWinesPerPage() {
                return _winesPerPage;
        }

        public static void SetWinesPerPage(int winesPerPage) {
                _winesPerPage = winesPerPage;
        }

        public static String GetWineName() {
                return _wineName;
        }

        public static void SetWineName(String wineName) {
                _wineName = wineName;
        }

        public static List<FacetQueryGroup> GetFacetQueryGroups() {
                return _facetQueryGroups;
        }

        public static void AddFacetQueryGroupValue(String name, String value) {
                for (FacetQueryGroup group : _facetQueryGroups) {
                        if (group.FieldName.equals(name)) {
                                group.Values.add(value);
                                return;
                        }
                }
                _facetQueryGroups.add(new FacetQueryGroup(name, value));
        }

        public static void RemoveFacetQueryGroupValue(String name, String value) {
                for (FacetQueryGroup group : _facetQueryGroups) {
                        if (group.FieldName.equals(name)) {
                                group.Values.remove(value);
                                if (group.Values.size() == 0) {
                                        _facetQueryGroups.remove(group);
                                }
                                return;
                        }
                }
        }

        public static void SetFacetQueryGroups(List<FacetQueryGroup> facetQueryGroups) {
                _facetQueryGroups = facetQueryGroups;
        }

}
