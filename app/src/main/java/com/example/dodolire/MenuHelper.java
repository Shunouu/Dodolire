package com.example.dodolire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Classe utilitaire pour gérer le menu burger dans toutes les activités
 */
public class MenuHelper {

    /**
     * Affiche le menu burger avec les options Formulaire et Archive
     * @param context Le contexte de l'activité
     * @param anchorView La vue sur laquelle le menu sera ancré (icône burger)
     */
    public static void showBurgerMenu(Context context, View anchorView) {
        // Créer la vue du menu
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_menu, null);

        // Créer la fenêtre popup
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        // Configurer les options du menu
        TextView formOption = popupView.findViewById(R.id.menu_form);
        TextView archiveOption = popupView.findViewById(R.id.menu_archive);

        // Option Formulaire
        formOption.setOnClickListener(v -> {
            Intent intent = new Intent(context, FormActivity.class);
            context.startActivity(intent);
            popupWindow.dismiss();

            // Si on est déjà dans FormActivity, ne pas fermer l'activité actuelle
            if (!(context instanceof FormActivity)) {
                ((Activity) context).finish();
            }
        });

        // Option Archive
        archiveOption.setOnClickListener(v -> {
            Intent intent = new Intent(context, ArchiveActivity.class);
            context.startActivity(intent);
            popupWindow.dismiss();

            // Si on est déjà dans ArchiveActivity, ne pas fermer l'activité actuelle
            if (!(context instanceof ArchiveActivity)) {
                ((Activity) context).finish();
            }
        });

        // Afficher le menu
        popupWindow.setElevation(10);
        popupWindow.showAsDropDown(anchorView, 0, 0, Gravity.END);
    }
}
