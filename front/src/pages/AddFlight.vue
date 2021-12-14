<template>
  <q-page padding>
    <!-- content -->
    <div>
      <q-form>
        <div class="row q-px-md full-width justify-between content-center align-center">
          <q-input class="col-6" bottom-slots v-model="origem" label="Origem">
            <template v-slot:before>
              <q-icon name="location_on" />
            </template>

            <template v-slot:append>
              <q-icon
                v-if="origem !== ''"
                name="close"
                @click="origem = ''"
                class="cursor-pointer"
              />
            </template>
          </q-input>
          <q-input class="col-6 q-px-md" bottom-slots v-model="destino" label="Destino">
            <template v-slot:before>
              <q-icon name="flight_takeoff" />
            </template>

            <template v-slot:append>
              <q-icon
                v-if="destino !== ''"
                name="close"
                @click="destino = ''"
                class="cursor-pointer"
              />
            </template>
          </q-input>
          <q-input
            v-model.number="nPassageiros"
            label="Quantidade"
            type="number"
            filled
            class="col-4 q-px-md"
          />
          <q-input
            v-model.number="precoBase"
            label="Preço"
            type="number"
            filled
            prefix="$"
            class="col-4 q-px-md"
          />
          <q-input
            v-model.number="tempoBase"
            label="Tempo de voo"
            type="number"
            filled
            prefix="Horas"
            class="col-4 q-px-md"
          />
          <div class="q-pa-md q-gutter-sm">
            <q-btn label="Cadastrar Voo" color="primary" icon="add" @click="addFlight" />
          </div>
        </div>
      </q-form>
    </div>
  </q-page>
</template>

<script>
  export default {
    // name: 'PageName',
    data() {
      return {
        origem: '',
        destino: '',
        nPassageiros: 0,
        precoBase: 0,
        companhia: process.env.ID,
        tempoBase: 0,
      };
    },
    methods: {
      addFlight() {
        this.$api
          .get('/addVoo', {
            params: {
              origem: this.origem,
              destino: this.destino,
              nPassageiros: this.nPassageiros,
              precoBase: this.precoBase,
              companhia: this.companhia,
              tempoBase: this.tempoBase,
            },
          })
          .then((response) => {
            this.addTicket = true;
            this.tempTickets = response.data;
            this.$q.notify({
              message: response.data,
              color: 'primary',
            });
          });
      },
    },
  };
</script>
