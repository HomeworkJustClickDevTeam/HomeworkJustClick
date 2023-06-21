import { PropsForGroupItem } from "../../../types/types"
import { Link } from "react-router-dom"

function GroupItem({ group }: PropsForGroupItem) {
  return (
    <>
      <Link to={`/group/${group.id}/assignments`} state={{ group: group }}>
        <h1>{group.name}</h1>{" "}
      </Link>
      <p>{group.description}</p>
    </>
  )
}
export default GroupItem
