import { BsTrash, BsPlusCircle } from "react-icons/bs";

function EditableUserList({users, leage_users = users, on_add, on_delete}) {

    const inLeague = (user) => {
        let c = leage_users.filter(lU => lU.appUserId === user.appUserId);
        return c.length > 0
    }

    return (
        <div className="flex flex-col items-center max-w-md mx-auto mb-4">
            <h2 className=" font-survivor text-xl mb-4">Users</h2>
            <div className=" border-2 border-black overflow-x-auto relative shadow-md sm:rounded-lg ">
                <table className="w-full text-sm text-left">
                    <thead className="table-header-group bg-black text-white">
                        <tr>
                            <th className="text-center">Users</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.filter(u=>inLeague(u)).map((user, index) => (
                            <tr className="border-b bg-Green"key={index}>
                                <td>{user.username}</td>
                                <td><button className="btn-red p-2 w-fit" onClick={() => on_delete(user)} ><BsTrash /></button></td>
                            </tr>
                        ))}
                        {users.filter(u=>!inLeague(u)).map((user, index) => (
                            <tr className={`border-b bg-slate-400`} key={index}>
                                <td>{user.username}</td>
                                <td><button className="btn-blue p-2 w-fit" onClick={() => on_add(user)} ><BsPlusCircle /></button></td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    )

}

export default EditableUserList;