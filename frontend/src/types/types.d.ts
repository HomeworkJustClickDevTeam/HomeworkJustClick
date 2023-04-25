interface RegisterUser{
    firstname:string,
    lastname: string,
    email:string,
    password:string
}
interface LoginUser {
    email:string,
    password: string
}
interface Group{
    id: number,
    name : string,
    description: string,
    color: number
}
interface GroupCreate{
    name:string,
    description:string,
}
interface UserToShow{
    firstname:string,
    lastname:string,
    email: string,
    index:number,
    id:number,
}

type PropsForLogin = {
    setLoggedIn : (loggedIn: boolean) => void
}

type PropsForGroupItem = {
    group:Group,
    key:number
}
type PropsForFiltering = {
    setGroups: (groups: Group []) => void
}