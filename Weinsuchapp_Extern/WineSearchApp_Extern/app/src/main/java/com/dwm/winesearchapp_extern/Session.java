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

        public static void AddFacetQueryGroupValue(FacetQueryGroup facetQueryGroup) {
                for (FacetQueryGroup group : _facetQueryGroups) {
                        if (group.FieldName.equals(facetQueryGroup.FieldName)) {
                                group.Values.add(facetQueryGroup.Values.get(0));
                                return;
                        }
                }
                _facetQueryGroups.add(facetQueryGroup);
        }

        public static void RemoveFacetQueryGroupValue(FacetQueryGroup facetQueryGroup) {
                for (FacetQueryGroup group : _facetQueryGroups) {
                        if (group.FieldName.equals(facetQueryGroup.FieldName)) {
                                group.Values.remove(facetQueryGroup.Values.get(0));
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
