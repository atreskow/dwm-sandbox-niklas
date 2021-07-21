package com.dwm.dwm_app.infrastructure;

import com.dwm.dwm_app.server_connection.request.FacetQueryGroup;
import com.dwm.dwm_app.adapters.WineListItem;

import java.util.ArrayList;
import java.util.List;

public class Session {
        private static boolean _startupCompleted = false;
        private static int _maxWinesSearch;
        private static int _winesPerPage = 10;
        private static String _searchText = null;
        private static List<FacetQueryGroup> _facetQueryGroups = new ArrayList<>();
        private static WineListItem _selectedListItem;
        private static int _selectedSpinnerSearch = 0;


        public static boolean getStartupCompleted() {
                return _startupCompleted;
        }

        public static void setStartupCompleted(boolean completed) {
                _startupCompleted = completed;
        }

        public static WineListItem getSelectedListItem() {
                return _selectedListItem;
        }

        public static void setSelectedListItem(WineListItem selectedListItem) {
                _selectedListItem = selectedListItem;
        }

        public static int getSelectedSpinnerSearch() {
                return _selectedSpinnerSearch;
        }

        public static void setSelectedSpinnerSearch(int selectedSpinnerSearch) {
                _selectedSpinnerSearch = selectedSpinnerSearch;
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

        public static String getSearchText() {
                return _searchText;
        }

        public static void setSearchText(String wineName) {
                _searchText = wineName;
        }

        public static List<FacetQueryGroup> getFacetQueryGroups() {
                return _facetQueryGroups;
        }

        public static void addFacetQueryGroupValue(String name, String value) {
                Utils.addFacetQueryGroupValue(_facetQueryGroups, name, value);
        }

        public static void removeFacetQueryGroupValue(String name, String value) {
                Utils.removeFacetQueryGroupValue(_facetQueryGroups, name, value);
        }

        public static void setFacetQueryGroups(List<FacetQueryGroup> facetQueryGroups) {
                _facetQueryGroups = facetQueryGroups;
        }
}
