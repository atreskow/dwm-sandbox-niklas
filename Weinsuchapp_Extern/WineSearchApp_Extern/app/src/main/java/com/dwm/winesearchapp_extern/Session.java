package com.dwm.winesearchapp_extern;

import com.dwm.winesearchapp_extern.Pojo.request.FacetQueryGroup;
import com.dwm.winesearchapp_extern.Pojo.response.WineData;

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
