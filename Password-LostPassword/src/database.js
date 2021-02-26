exports.getUserEmail = getUserEmail
exports.updateUserAccount = updateUserAccount
exports.deleteRequest = deleteRequest
exports.openConnection = openConnection

const path = require('path')
const fs = require('fs')
const postgres = require('postgres')

let sql = null;

async function openConnection(config) {
    sql = postgres({
        host: config.dbHostName,
        port: config.dbPort,
        username: config.dbUserName,
        password: config.dbPassword,
        database: 'passwordmanager'
    })
}

async function getUserEmail(requestId) {
    try {
    const emails = await sql`
        select 
            email 
        from public."ResetRequests"
        where 
            id = ${ requestId }`
    
    console.log(emails)

    return emails[0].email
    } catch (err) {
        if (err instanceof TypeError) {
            return ''
        }

        throw err
    }
}

async function updateUserAccount(userAccount) {
    let query = 'UPDATE Accounts SET Password = ? WHERE Email = ?'

    const updatedUser = await sql`
        update 
            public."Accounts" 
        set "Password" = ${ userAccount.password }
        where 
            "Email" = ${ userAccount.email }
        returning *`
}

async function deleteRequest(requestId) {
    var requestIdNum = Number(requestId)

    let query = 'DELETE FROM ResetRequests WHERE id = ?'

    const deletedRequest = await sql`
        delete 
        from public."ResetRequests" 
        where 
            id = ${ requestId }
        returning *`
}