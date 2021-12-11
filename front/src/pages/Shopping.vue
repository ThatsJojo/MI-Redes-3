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
      <q-card>
        <q-toolbar class="bg-primary text-white shadow-2">
          <q-toolbar-title>Caminhos</q-toolbar-title>
        </q-toolbar>
        <q-item v-for="way,index in ways" :key="index">
          <q-card class="row full-width content-center align-center q-pa-md">
            <text class="text-weight-bold">Opçao {{index +1}}:</text>
            <div v-for="airport, indexAirport in way" :key="airport.id">
              <q-avatar icon="flight_takeoff" />
              {{airport.nome}}
              <q-icon
                :key="airport.id +'' + index"
                v-if="indexAirport < way.length -1"
                size="1.2em"
                name="arrow_forward"
                color="purple"
              />
              <q-tree
                :nodes="way"
                default-expand-all
                node-key="nome"
                @lazy-load="onLazyLoad(indexAirport + 1)"
              />
            </div>
          </q-card>
        </q-item>
      </q-card>
    </div>
  </q-page>
</template>

<script>
  export default {
    // name: 'PageName',
    data() {
      return {
        placeAt: '',
        placeFrom: '',
        ways: [],
      };
    },
    methods: {
      searchRoutes() {
        this.$api
          .get('/caminho', {
            params: { origem: this.placeFrom, destino: this.placeAt },
          })
          .then((response) => {
            console.log(response);
            this.ways = response.data;
          });
      },
    },
  };
</script>
