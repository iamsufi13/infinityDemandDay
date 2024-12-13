package com.contenttree.admin;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class DashboardWidgetsResponse {
    private List<Widget> ecomWidgets;
    private List<Widget> totalecomWidgets;

    public List<Widget> getEcomWidgets() {
        return ecomWidgets;
    }

    public void setEcomWidgets(List<Widget> ecomWidgets) {
        this.ecomWidgets = ecomWidgets;
    }

    public List<Widget> getTotalecomWidgets() {
        return totalecomWidgets;
    }

    public void setTotalecomWidgets(List<Widget> totalecomWidgets) {
        this.totalecomWidgets = totalecomWidgets;
    }

}
