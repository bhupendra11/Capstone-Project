package quickjournal.bhupendrashekhawat.me.android.quickjournal;

import java.util.Random;

/**
 * Created by Bhupendra Shekhawat on 21/11/16.
 */

public class MotiveQuotes {

    public static String getQuote() {

        String jokesArr[] = {
                "Winners are not people who never fail, but people who never quit."
                ,
                "Your mind is a powerful thing.Fill it with positive thoughts and your life will start to change."
                ,
                "You never know how strong you are until being strong is your only option."
                ,
                "Stop saying \"Iwish\" start saying \"I will\""
                ,

                "Everyday is a new opportunity to have a new start."
                ,
                "Work hard in silence. Let your success be your noise."
                ,
                "The harder you work , the harder it is to surrender"
                ,
                "Being busy and buing productive are two different things."
                ,

                "Success is never owned.It is rented and that rent is due everyday."
                ,

                "The best preperation for good tomorrow is to do good work today.",

                "There is no secret to success. It is the result of preparation, hard work and learning from failure."
                ,

                "I am a great believer in luck, and I find the harder I work, the more I have of it.",

                "Believe you can and you are halfway there."

        };

        //Choosing a random joke id
        Random random = new Random();
        int randomJokeId = random.nextInt(jokesArr.length);

        return jokesArr[randomJokeId];


    }
}
