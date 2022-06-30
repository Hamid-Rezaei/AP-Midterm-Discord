package controller;

import model.Chat;
import model.User;
import model.guild.*;

import java.io.*;
import java.net.*;
import java.util.*;

import static view.MenuHandler.sc;

public class AppController {


    public enum ServerErrorType {
        NO_ERROR(1), USER_ALREADY_EXISTS(2), SERVER_CONNECTION_FAILED(3), DATABASE_ERROR(4), Duplicate_ERROR(5), ALREADY_FRIEND(6), UNKNOWN_ERROR(404);

        private int code;

        ServerErrorType(int code) {
            this.code = code;
        }
    }

    private User currentUser;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public AppController() {
        setupConnection();
    }


    private void setupConnection() {
        try {
            socket = new Socket("localhost", 7777);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException x) {
            System.out.println("SERVER CONNECTION FAILED.");
            System.exit(0);
        }
    }


    public String signUp(String username, String password, String email, String phoneNum, InputStream avatar) {

        try {
            outputStream.writeUTF("#signUp");
            outputStream.flush();
            outputStream.writeUTF(username + " " + password + " " + email + " " + phoneNum);
            outputStream.flush();
            byte[] img = avatar.readAllBytes();
            outputStream.writeInt(img.length);
            outputStream.flush();
            outputStream.write(img, 0, img.length);
            outputStream.reset();
            return parseError(inputStream.readInt());
        } catch (IOException x) {
            x.printStackTrace();
            return "IOException";
        }
    }


    public User login(String username, String password) {
        try {
            outputStream.writeUTF("#login");
            outputStream.flush();
            outputStream.writeUTF(username + " " + password);
            outputStream.flush();
            User user = (User) inputStream.readObject();
            if (user == null) {
                return user;
            }
            int avatarSize = inputStream.readInt();
            byte[] avatar = new byte[avatarSize];
            inputStream.readFully(avatar, 0, avatarSize);
            user.setAvatar(avatar);
            currentUser = user;
            return user;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can not write for server.");
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Can not read from server.");
            return null;
        }
    }


    public String friendRequest(String username, String targetUser) {
        String answer;
        int answerCode;
        try {
            outputStream.writeUTF("#friendRequest");
            outputStream.flush();

            outputStream.writeUTF(username);
            outputStream.flush();

            outputStream.writeUTF(targetUser);
            outputStream.flush();
            answerCode = inputStream.readInt();
            answer = parseError(answerCode);
        } catch (IOException e) {
            e.printStackTrace();
            answer = "something went wrong with friend Request.";
        }
        return answer;
    }

    public HashSet<String> friendRequestList(String username) {
        try {
            outputStream.writeUTF("#RequestList");
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            return (HashSet<String>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String revisedFriendRequests(String username, HashSet<String> accepted, HashSet<String> rejected) {
        try {
            outputStream.writeUTF("#revisedFriendRequests");
            outputStream.flush();
            outputStream.writeObject(accepted);
            outputStream.flush();
            outputStream.writeObject(rejected);
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            String response = inputStream.readUTF();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return "something went wrong while revising Friend requests.";
        }
    }

    public HashSet<String> friendList(String username) {
        try {
            outputStream.writeUTF("#FriendList");
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            return (HashSet<String>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUser(String username) {
        try {
            outputStream.writeUTF("#getUser");
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            return (User) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String blockUser(String username) {
        try {
            outputStream.writeUTF("#blockUser");
            outputStream.writeUTF(currentUser.getUsername());
            outputStream.flush();
            outputStream.writeUTF(username);
            outputStream.flush();
            String dbResponse = inputStream.readUTF();
            return dbResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return "something went wrong while blocking user.";
        }
    }

    public String unblockUser(String unblockTarget) {
        try {
            outputStream.writeUTF("#unblockUser");
            outputStream.flush();
            outputStream.writeUTF(currentUser.getUsername());
            outputStream.flush();
            outputStream.writeUTF(unblockTarget);
            outputStream.flush();
            String respone = inputStream.readUTF();
            return respone;
        } catch (IOException e) {
            e.printStackTrace();
            return "something went wrong while unblocking user.";
        }
    }


    public HashSet<String> blockedList() {
        try {
            outputStream.writeUTF("#blockList");
            outputStream.flush();
            outputStream.writeUTF(currentUser.getUsername());
            outputStream.flush();
            HashSet<String> blockedList = (HashSet<String>) inputStream.readObject();
            return blockedList;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Chat requestForDirectChat(User friend) {
        try {
            outputStream.writeUTF("#requestForDirectChat");
            outputStream.flush();
            outputStream.writeObject(friend);
            outputStream.flush();
            outputStream.writeObject(currentUser);
            outputStream.flush();
            String answer = inputStream.readUTF();
            if (answer.equals("Success")) {
                Chat directChat = (Chat) inputStream.readObject();
                directChat.setCurrUser(currentUser);
                directChat.setOutputStream(outputStream);
                directChat.setInputStream(inputStream);
                Thread chatThread = new Thread(directChat);
                chatThread.start();
                chatThread.join();
//                new Thread(directChat).start();
                return directChat; // "You are in private chat with " + friend.getUsername();
            } else {
                return null;
            }

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
            return null;//"Could not open chat with " + friend.getUsername();
        }
    }

    public void removeFromDirectChat(User user, User friend) {
        try {
            outputStream.writeUTF("#removeFromChat");
            outputStream.flush();
            outputStream.writeUTF(user.getUsername());
            outputStream.flush();
            outputStream.writeUTF(friend.getUsername());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String addServer(Guild guild) {
        try {
            outputStream.writeUTF("#addGuild");
            outputStream.flush();
            outputStream.writeObject(guild);
            outputStream.flush();
            return inputStream.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
            return "Fail to add server.";
        }
    }

    public ArrayList<Guild> listOfJoinedServers() {
        try {
            outputStream.writeUTF("#serverList");
            outputStream.flush();
            outputStream.writeUTF(currentUser.getUsername());
            outputStream.flush();
            ArrayList<Guild> guilds = (ArrayList<Guild>) inputStream.readUnshared();
            return guilds;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Guild getGuild(String owner, String guildName) {
        try {
            outputStream.writeUTF("#getGuild");
            outputStream.flush();
//            outputStream.writeUTF(owner);
//            outputStream.flush();
            outputStream.writeUTF(guildName);
            outputStream.flush();
            Guild guild = (Guild) inputStream.readUnshared();
            return guild;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String addMemberToServer(String name, Guild guild) {
        try {
            User user = getUser(name);
            GuildUser member = new GuildUser(user, new Role("member"));
            outputStream.writeUTF("#addMember");
            outputStream.flush();
            outputStream.writeObject(member);
            outputStream.flush();
            outputStream.writeUTF(guild.getOwnerName());
            outputStream.flush();
            outputStream.writeUTF(guild.getName());
            outputStream.flush();
            String respond = inputStream.readUTF();
            return respond;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "something went wrong while adding member to server.";
    }

    public String addNewTextChannel(Guild guild) {
        String response = null;
        try {
            System.out.print("Enter text channel name: ");
            String name = sc.nextLine();
            outputStream.writeUTF("#addTextChannel");
            outputStream.flush();
            outputStream.writeUTF(name);
            outputStream.flush();
            outputStream.writeUTF(guild.getOwnerName());
            outputStream.flush();
            outputStream.writeUTF(guild.getName());
            outputStream.flush();
            response = inputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            response = "something went wrong while adding text channel";
        }
        return response;
    }

    public void requestForGroupChat(Guild guild,TextChannel textChannel) {
        try {
            outputStream.writeUTF("#getTextChannel");
            outputStream.flush();
            outputStream.writeUTF(guild.getOwnerName());
            outputStream.flush();
            outputStream.writeUTF(guild.getName());
            outputStream.flush();
            outputStream.writeUTF(textChannel.getName());
            outputStream.flush();
            String answer = inputStream.readUTF();
            if(answer.equals("success.")){
                Chat groupChat = new Chat();
                groupChat.setOutputStream(outputStream);
                groupChat.setInputStream(inputStream);
                groupChat.setCurrUser(currentUser);
                Thread groupChatThread = new Thread(groupChat);
                groupChatThread.start();
                groupChatThread.join();
            } else{
                System.out.println("something went wrong while requesting for group chat.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //TODO : check if this function works
    public String deleteMemberFromServer(String name, Guild guild) {
        try {
            User user = getUser(name);
            GuildUser member = new GuildUser(user, new Role("member"));
            outputStream.writeUTF("#removeMember");
            outputStream.flush();
            outputStream.writeObject(member);
            String respond = inputStream.readUTF();
            return respond;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "something went wrong while removing member from server.";
    }

    public String changeGuildName(Guild guild, String newName) {
        try {
            outputStream.writeUTF("#changeGuildName");
            outputStream.flush();
            outputStream.writeUTF(guild.getOwnerName());
            outputStream.flush();
            outputStream.writeUTF(guild.getName());
            outputStream.flush();
            outputStream.writeUTF(newName);
            outputStream.flush();
            return inputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "something went wrong while renaming server.";
    }

    public String parseError(int errorCode) {
        String error;
        switch (errorCode) {
            case 1:
                error = "Success";
                break;
            case 2:
                error = "This user already exists.";
                break;
            case 3:
                error = "Connection with server failed.";
                break;
            case 4:
                error = "There was a problem with database.";
                break;
            case 5:
                error = "you already have a friend request with this user.";
                break;
            case 6:
                error = "you are already friend of this user.";
                break;
            default:
                error = "UNKNOWN ERROR.";
                break;

        }
        return error;
    }

}