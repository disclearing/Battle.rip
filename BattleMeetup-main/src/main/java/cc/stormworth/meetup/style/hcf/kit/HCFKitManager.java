package cc.stormworth.meetup.style.hcf.kit;

import cc.stormworth.meetup.style.hcf.kit.impl.backpack.BackpackKit;
import cc.stormworth.meetup.style.hcf.kit.impl.dueler.DuelerKit;
import cc.stormworth.meetup.style.hcf.kit.impl.focus.FocusKit;
import cc.stormworth.meetup.style.hcf.kit.impl.guardian.GuardianKit;
import cc.stormworth.meetup.style.hcf.kit.impl.insanestrength.InsaneStrengthBowKit;
import cc.stormworth.meetup.style.hcf.kit.impl.insanestrength.InsaneStrengthKit;
import cc.stormworth.meetup.style.hcf.kit.impl.killjoy.KilljoyKit;
import cc.stormworth.meetup.style.hcf.kit.impl.phoenix.PhoenixKit;
import cc.stormworth.meetup.style.hcf.kit.impl.pocketbard.PocketBardKit;
import cc.stormworth.meetup.style.hcf.kit.impl.reyna.ReynaKit;
import cc.stormworth.meetup.style.hcf.kit.impl.sage.SageKit;
import cc.stormworth.meetup.style.hcf.kit.impl.tryhard.TryhardKit;

public class HCFKitManager {

    public HCFKitManager() {
        new BackpackKit();
        new DuelerKit();
        new FocusKit();
        new GuardianKit();
        new InsaneStrengthKit();
        new InsaneStrengthBowKit();
        new KilljoyKit();
        new PhoenixKit();
        new PocketBardKit();
        new ReynaKit();
        new SageKit();
        new TryhardKit();
    }
}
