import java.util.ArrayList;
import java.util.Collections;

public class Management
{
    public static final int teamMax = 2;
    public static int round;
    public static ArrayList<Assassin> playerList = new ArrayList<Assassin>();
    public static ArrayList<Assassin> alivePlayers = new ArrayList<Assassin>();
    public static ArrayList<Assassin> deadPlayers = new ArrayList<Assassin>();
    public static ArrayList<Team> teamList = new ArrayList<Team>();
    public static ArrayList<Team> aliveTeams = new ArrayList<Team>();
    public static ArrayList<Team> deadTeams = new ArrayList<Team>();
    public static Team winner = null;

    public static void assignFromKill(Assassin player, Assassin target)
    {
        player.getTeam().setTarget(target.getTeam().getTarget());
        target.getTeam().setTarget(null);

        if(checkWin())
        {
            System.out.println("Team " + winner.getName() + " has won Assassins.");
        }
    }

    public static void clearLists()
    {
        playerList.clear();
        alivePlayers.clear();
        deadPlayers.clear();
        teamList.clear();
        aliveTeams.clear();
        deadTeams.clear();
    }
    public static void randomAssignment()
    {
        ArrayList<Team> temp = new ArrayList<Team>();
        temp.addAll(Management.aliveTeams);
        Collections.shuffle(temp);

        for (int i = 0; i < temp.size(); i++)
        {
            Team team = temp.get(i);
            if (i == temp.size() - 1)
            {
                team.setTarget(temp.get(0));
            }
            else
            {
                team.setTarget(temp.get(i+1));
            }
        }
    }

    public static void startGame()
    {
        round = 1;
        randomAssignment();
    }
    public static void endRound() {
        // Loop through all teams and checks if they got a kill this round
        // If they did, they move through to the next
        // If not, both members are eliminated and the team is out
        String message;

        for (int i = 0; i < aliveTeams.size(); i++)
        {
            Team team = aliveTeams.get(i);
            Assassin member1 = team.getMembers()[0];
            Assassin member2 = team.getMembers()[1];

            System.out.println("Team " + i + "/" + aliveTeams.size() + " - " + team.getName());

            if(team.getImmune())
            {
                message = "Team " + team.getName() + " is immune from elimination this round.";
                System.out.println(message);
                LogHandler.addLog(message);
                team.setImmune(false);
                member1.setKillsThisRound(0);
                member2.setKillsThisRound(0);
            }
            else if (team.getKillsThisRound() == 0)
            {
                team.eliminate();
                member1.eliminate();
                member2.eliminate();
                i--;
                message = "Team " + team.getName() + " was eliminated due to not getting a kill in Round " + round;
                System.out.println(message);
                LogHandler.addLog(message);
            }
            else {
                message = "Team " + team.getName() + " successfully moves forward to the next round.";
                System.out.println(message);
                LogHandler.addLog(message);
                member1.setKillsThisRound(0);
                member2.setKillsThisRound(0);
            }
        }

        randomAssignment();
        round++;
        message = "Round " + round + " has begun";
        LogHandler.addLog(message);

        if(checkWin())
        {
            System.out.println("Team " + winner.getName() + " has won Assassins.");
        }
    }

    private static boolean checkWin()
    {
        if (aliveTeams.size() == 1)
        {
            winner = aliveTeams.get(0);
            return true;
        }
        return false;
    }

    public static void manualAssign(Team receive, Team give)
    {
        receive.setTarget(give.getTarget());
        give.setTarget(null);
    }

}