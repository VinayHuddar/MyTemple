package com.mytemple.androidapp.MyTemple.View;

import com.mytemple.androidapp.MyTemple.View.EventsFragment;
import com.mytemple.androidapp.R;

/**
 * Created by vinayhuddar on 22/09/15.
 */
public class DemoContent_MarutiMandir {
    public static final int[] MainScreenFlipperImages = {
            R.drawable.gallery1_mm_big,
            R.drawable.gallery2_mm_big,
            R.drawable.gallery3_mm_big,
            R.drawable.gallery4_mm_big
    };

    public static final int TempleTiming = R.string.pooja_timing;

    public static final int AboutTemple = R.string.temple_about_text;

    public static final int[] MilestoneDates = {R.string.history1_date, R.string.history2_date,
            R.string.history3_date, R.string.history4_date};
    public static final int[] MilestoneDrawables = {R.drawable.history1_kannada, R.drawable.history2_kannada,
            R.drawable.history3_kannada, R.drawable.history4_kannada};
    //public static final int[] Milestones = {R.string.history1, R.string.history2};

    public static class Seva {
        int sevaNameStringId;
        int price;

        public Seva(int id, int _price) {
            sevaNameStringId = id;
            price = _price;
        }
    }
    public static final Seva[] RegularSevaList = {
        new Seva(R.string.seva_list_satyanarayana_pooja, 40),
        new Seva(R.string.seva_list_vade_maale_seve_54, 280),
        new Seva(R.string.seva_list_vade_maale_seve_108, 380),
        new Seva(R.string.seva_list_kadubina_alankaara_54, 280),
        new Seva(R.string.seva_list_kadubina_alankaara_108, 380),
        new Seva(R.string.seva_list_viled_yele_alankaara, 300),
        new Seva(R.string.seva_list_everyday_prasada, 350),
        new Seva(R.string.seva_list_abhisheka, 450),
        new Seva(R.string.seva_list_naga_deva_pooje, 500),
        new Seva(R.string.seva_list_navagraha_pooje, 500),
        new Seva(R.string.seva_list_navagraha_shanti, 1200),
        new Seva(R.string.seva_list_butter_alankaara, 4000),
        new Seva(R.string.seva_list_cashew_alankaara, 5000),
        new Seva(R.string.seva_list_special_flower_alankaara, 5000),
        new Seva(R.string.seva_list_fruits_flowers_alankaara, 750),
        new Seva(R.string.seva_list_tomaala_seve, 125)
    };

    public static class Pujari {
        int pujariImageDrawable;
        int pujariName;
        int pujariAbout;

        public Pujari(int imageDrawable, int name, int about) {
            pujariImageDrawable = imageDrawable;
            pujariName = name;
            pujariAbout = about;
        }
    }
    public static final Pujari[] TemplePujariList = {
        new Pujari(R.drawable.pujari, R.string.pujari1, R.string.archakaru),
        new Pujari(R.drawable.pujari, R.string.pujari2, R.string.pujari),
        new Pujari(R.drawable.pujari, R.string.pujari3, R.string.pujari),
        new Pujari(R.drawable.pujari, R.string.pujari4, R.string.pujari),
    };

    public static final int TrustName = R.string.trust_name;
    public static final int TrustAddress = R.string.trust_address;
    public static class Trustee {
        int imageDrawable;
        int name;
        int designation;

        public Trustee(int _imageDrawable, int _name, int _designation) {
            imageDrawable = _imageDrawable;
            name = _name;
            designation = _designation;
        }
    }
    public static final Trustee[] Trust = {
        new Trustee(R.drawable.trustee, R.string.trustee1, R.string.chairman_label),
        new Trustee(R.drawable.trustee, R.string.trustee2, R.string.secretary_label),
        new Trustee(R.drawable.trustee, R.string.trustee3, R.string.treasurer_label),
        new Trustee(R.drawable.trustee, R.string.trustee4, R.string.trustee_label),
        new Trustee(R.drawable.trustee, R.string.trustee5, R.string.trustee_label),
        new Trustee(R.drawable.trustee, R.string.trustee6, R.string.trustee_label),
        new Trustee(R.drawable.trustee, R.string.trustee7, R.string.trustee_label),
        new Trustee(R.drawable.trustee, R.string.trustee8, R.string.trustee_label),
        new Trustee(R.drawable.trustee, R.string.trustee9, R.string.trustee_label),
        new Trustee(R.drawable.trustee, R.string.trustee10, R.string.trustee_label),
        new Trustee(R.drawable.trustee, R.string.trustee11, R.string.trustee_label)
    };

    public static final class SpecialEvent {
        int eventType;
        int title;
        int date;
        int description;

        public SpecialEvent (int _eventType, int _title, int _date, int _description) {
            eventType = _eventType;
            title = _title;
            date = _date;
            description = _description;
        }
    }

    public static final SpecialEvent[] SpecialEventData = {
        new SpecialEvent(EventsFragment.EVENT_TYPE_ABHISHEKA, R.string.special_event1_title,
                R.string.special_event1_date, R.string.special_event1_description),
        new SpecialEvent(EventsFragment.EVENT_TYPE_POOJA, R.string.special_event2_title,
                R.string.special_event2_date, R.string.special_event2_description)
    };

    public static final int[] GalleryImages = {
            R.drawable.gallery1_mm,
            R.drawable.gallery2_mm,
            R.drawable.gallery3_mm,
            R.drawable.gallery4_mm,
    };
    public static final int[] GalleryImagesBig = {
            R.drawable.gallery1_mm_big,
            R.drawable.gallery2_mm_big,
            R.drawable.gallery3_mm_big,
            R.drawable.gallery4_mm_big
    };
}
