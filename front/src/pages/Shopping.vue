<template>
  <q-page padding>
    <!-- content -->
    <div>
      <q-form>
        <div class="row full-width justify-between content-center align-center">
          <q-input class="col-5" bottom-slots v-model="placeFrom" label="Origem">
            <template v-slot:before>
              <q-icon name="location_on" />
            </template>

            <template v-slot:append>
              <q-icon
                v-if="placeFrom !== ''"
                name="close"
                @click="placeFrom = ''"
                class="cursor-pointer"
              />
            </template>
          </q-input>
          <q-input class="col-5" bottom-slots v-model="placeAt" label="Destino">
            <template v-slot:before>
              <q-icon name="flight_takeoff" />
            </template>

            <template v-slot:append>
              <q-icon
                v-if="placeAt !== ''"
                name="close"
                @click="placeAt = ''"
                class="cursor-pointer"
              />
            </template>
          </q-input>
          <div class="q-pa-md q-gutter-sm">
            <q-btn color="primary" icon="search" @click="searchRoutes" />
          </div>
        </div>
      </q-form>
    </div>
    <div>
      <q-card v-if="tickets.length > 0">
        <q-toolbar class="bg-primary text-white shadow-2">
          <q-toolbar-title>Selecionados</q-toolbar-title>
          <q-space />
          <text
            class="q-mx-sm"
          >Preço total: {{tickets.reduce((carry, e)=> e.precoBase + carry, 0)}} R$</text>
          <text
            class="q-mx-sm"
          >Tempo total: {{tickets.reduce((carry, e)=> e.tempoBase + carry, 0)}} Horas</text>
          <q-input bottom-slots v-model="nome" label="Nome comprador" />

          <q-btn label="Comprar" color="positive" @click="buy"></q-btn>
        </q-toolbar>
        <div class="row">
          <q-card class="col-3 my-card p-pa-lg" v-for="ticket, index in tickets" :key="index">
            <q-list>
              <q-item clickable>
                <q-item-section avatar>
                  <q-icon color="primary" name="place" />
                </q-item-section>

                <q-item-section>
                  <q-item-label>Companhia: {{ticket.companhia.nome}}</q-item-label>
                  <q-item-label caption></q-item-label>
                </q-item-section>
              </q-item>
              <q-item clickable>
                <q-item-section>
                  <q-item-label>De: {{ticket.origem.nome}}</q-item-label>
                  <q-icon name="flight_takeoff" />
                  <q-item-label>Até: {{ticket.destino.nome}}</q-item-label>
                </q-item-section>
              </q-item>
              <q-item clickable>
                <q-item-section>
                  <q-item-label>Tempo: {{ticket.tempoBase}} horas</q-item-label>
                  <q-item-label>Preço: {{ticket.precoBase}} R$</q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
            <q-btn
              color="negative"
              class="q-ma-md"
              label="Remover"
              @click="()=> tickets = tickets.filter((e)=> e.id != ticket.id)"
            />
            <q-separator />
          </q-card>
        </div>
      </q-card>

      <q-card>
        <q-toolbar class="bg-primary text-white shadow-2">
          <q-toolbar-title>Caminhos</q-toolbar-title>
        </q-toolbar>
        <q-item v-for="way,index in ways" :key="index">
          <q-card class="row full-width content-center align-center q-pa-md">
            <text class="text-weight-bold q-ma-md">Opçao {{index +1}}:</text>
            <div
              class="row content-center align-center"
              v-for="airport, indexAirport in way"
              :key="airport.id"
            >
              <text class="q-mx-md">{{airport.nome}}</text>
              <q-btn
                v-if="indexAirport < way.length -1"
                icon="flight_takeoff"
                rounded
                @click="getFlights(airport.nome, way[indexAirport +1].nome)"
              ></q-btn>
            </div>
          </q-card>
        </q-item>
      </q-card>
      <q-dialog v-model="addTicket">
        <q-card class="my-card p-pa-lg" v-for="ticket, index in tempTickets" :key="index">
          <q-list>
            <q-item clickable>
              <q-item-section avatar>
                <q-icon color="primary" name="place" />
              </q-item-section>

              <q-item-section>
                <q-item-label>Companhia: {{ticket.companhia.nome}}</q-item-label>
                <q-item-label caption></q-item-label>
              </q-item-section>
            </q-item>
            <q-item clickable>
              <q-item-section>
                <q-item-label>De: {{ticket.origem.nome}}</q-item-label>
                <q-icon name="flight_takeoff" />
                <q-item-label>Até: {{ticket.destino.nome}}</q-item-label>
              </q-item-section>
            </q-item>
            <q-item clickable>
              <q-item-section>
                <q-item-label>Tempo: {{ticket.tempoBase}} horas</q-item-label>
                <q-item-label>Preço: {{ticket.precoBase}} R$</q-item-label>
                <q-item-label>Passagens: {{ticket.numeroDePassageiros}}</q-item-label>
                <q-item-label>Passagens Vendidas: {{Math.abs(ticket.passaagensVendidas)}}</q-item-label>
              </q-item-section>
            </q-item>
          </q-list>
          <q-btn color="primary" class="q-ma-md" label="Adicionar" @click="pushTicket(ticket)" />
          <q-separator />
        </q-card>
      </q-dialog>
    </div>
  </q-page>
</template>

<script>
  import { convertArrayToObject } from '../utils/functions';
  export default {
    // name: 'PageName',
    data() {
      return {
        placeAt: '',
        placeFrom: '',
        ways: [],
        tickets: [],
        tempTickets: [],
        addTicket: false,
        nome: '',
      };
    },
    methods: {
      searchRoutes() {
        this.tickets = [];
        this.$api
          .get('/caminho', {
            params: { origem: this.placeFrom, destino: this.placeAt },
          })
          .then((response) => {
            console.log(response);
            this.ways = response.data;
          });
      },
      pushTicket(ticket) {
        let exist = this.tickets.find((element) => {
          return element.id == ticket.id;
        });
        if (!exist) {
          this.tickets.push(ticket);
        }
        this.addTicket = false;

        console.log(this.tickets);
      },
      getFlights(origem, destino) {
        this.$api
          .get('/ticket', {
            params: { origem, destino },
          })
          .then((response) => {
            this.addTicket = true;
            this.tempTickets = response.data;
          });
      },
      buy() {
        let voos = this.tickets.map((ticket) => ticket.id);
        this.$api
          .post('/purchase', {
            comprar: 0,
            tamanho: this.tickets.length,
            nome: this.nome,
            voos,
          })
          .then((response) => {
            this.searchRoutes(this.placeAt, this.placeFrom);
            this.$q.notify({
              message: 'Passagem comprada',
              color: 'primary',
            });
          })
          .catch((error) => {
            this.$q.notify({
              message: 'Não foi possivel realizar compra',
              color: 'negative',
            });
          });
      },
    },
  };
</script>
