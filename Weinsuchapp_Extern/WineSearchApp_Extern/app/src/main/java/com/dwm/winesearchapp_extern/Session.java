package com.dwm.winesearchapp_extern;

import com.dwm.winesearchapp_extern.Pojo.request.FacetQueryGroup;

import java.util.ArrayList;
import java.util.List;

public class Session {
        private static int _page = 0;
        private static int _winesPerPage = 20;
        private static String _wineName = null;
        private static List<FacetQueryGroup> _facetQueryGroups = new ArrayList<>();

        public static int GetPage() {
                return _page;
        }

        public static void SetPage(int page) {
                _page = page;
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

        public static FacetQueryGroup[] GetFacetQueryGroupsArray() {
                return _facetQueryGroups.toArray(new FacetQueryGroup[0]);
        }

        public static void AddFacetQueryGroup(FacetQueryGroup facetQueryGroup) {
                _facetQueryGroups.add(facetQueryGroup);
        }

        public static void SetFacetQueryGroups(List<FacetQueryGroup> facetQueryGroups) {
                _facetQueryGroups = facetQueryGroups;
        }
}
