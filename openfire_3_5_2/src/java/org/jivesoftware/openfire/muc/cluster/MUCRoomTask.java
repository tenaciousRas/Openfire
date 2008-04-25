/**
 * $RCSfile: $
 * $Revision: $
 * $Date: $
 *
 * Copyright (C) 2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.openfire.muc.cluster;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.muc.spi.LocalMUCRoom;
import org.jivesoftware.util.cache.ClusterTask;
import org.jivesoftware.util.cache.ExternalizableUtil;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Task related to a room to be executed in a cluster node. This is a base
 * class to specific room tasks. The base class just keeps track of the room
 * related to the task.
 *
 * @author Gaston Dombiak
 */
public abstract class MUCRoomTask implements ClusterTask {
    private boolean originator;
    private LocalMUCRoom room;

    protected MUCRoomTask() {
    }

    protected MUCRoomTask(LocalMUCRoom room) {
        this.room = room;
    }

    public LocalMUCRoom getRoom() {
        return room;
    }

    public boolean isOriginator() {
        return originator;
    }

    public void setOriginator(boolean originator) {
        this.originator = originator;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ExternalizableUtil.getInstance().writeBoolean(out, originator);
        ExternalizableUtil.getInstance().writeSafeUTF(out, room.getName());
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        originator = ExternalizableUtil.getInstance().readBoolean(in);
        String roomName = ExternalizableUtil.getInstance().readSafeUTF(in);
        room = (LocalMUCRoom) XMPPServer.getInstance().getMultiUserChatServer().getChatRoom(roomName);
        if (room == null) {
            throw new IllegalArgumentException("Room not found: " + roomName);
        }
    }
}
