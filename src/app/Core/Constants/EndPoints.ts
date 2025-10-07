const apiUrl = "http://localhost:8080/efacture";
const authUrl = "http://localhost:8080/api/auth";

const devisapi = `${apiUrl}/devis`; 
const configurationapi = `${apiUrl}/configuration`; 

export const eFactureEndPoints = {
    Devis : {
        creation : `${devisapi}/creation`,
        construction : `${devisapi}/construction`,
        id : `${devisapi}/byid`, 
        all : `${devisapi}/all`,
    }, 
    Enregistrement :{
        allByDevisAndTache : `${devisapi}/impression-enregistrement`, 
        impressionTache : `${devisapi}/impression-tache`, 
        impressionComplete : `${devisapi}/impression-complete`, 
        download :`${configurationapi}/load`, 
        telechargementByDevisAndTache :  `${devisapi}/telechargement-devis-tache`
        
    }, 
    Materiel : {
        all : `${configurationapi}/materiel/all`, 
        create : `${configurationapi}/materiel/create`, 
    }, 
    Tache : {
        all : `${configurationapi}/tache/all`,
        create : `${configurationapi}/tache/create`,
        allTacheByDevis : `${devisapi}/allTache`, 
    }, 
    Auth :{
        register : `${authUrl}/register`,
        login : `${authUrl}/login`,
        fetch : `${authUrl}/fetch` //Permet de recupere l'id du technicien 
    }
}
