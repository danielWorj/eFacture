const apiUrl = "http://localhost:8080/efacture/api/v1";

const devisapi = `${apiUrl}/devis`; 
const configurationapi = `${apiUrl}/configuration`; 

export const eFactureEndPoints = {
    Devis : {
        creation : `${devisapi}/creation`,
        construction : `${devisapi}/construction`,
    }, 
    Enregistrement :{
        allByDevisAndTache : `${devisapi}/bydevis/tache`
    }, 
    Materiel : {
        all : `${configurationapi}/materiel/all`
    }, 
    Tache : {
        all : `${configurationapi}/tache/all`
    }
}
